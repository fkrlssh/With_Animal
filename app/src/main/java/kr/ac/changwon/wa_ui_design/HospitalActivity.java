package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HospitalActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private ListView hospitalListView;
    private List<Hospital> hospitalList;
    private HospitalListAdapter adapter;
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;
    private List<Hospital> filteredHospitalList; // 검색 결과 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital);

        // 돌아가기 버튼 설정
        ImageButton hospitalReturnMain = findViewById(R.id.hospital_return_home);
        hospitalReturnMain.setOnClickListener(view -> {
            Intent intent = new Intent(HospitalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        hospitalListView = findViewById(R.id.hospital_list);
        hospitalList = new ArrayList<>();
        filteredHospitalList = new ArrayList<>(hospitalList);
        adapter = new HospitalListAdapter(this, filteredHospitalList);
        hospitalListView.setAdapter(adapter);
        EditText hospitalSearch = findViewById(R.id.hospital_search); // 검색 에디터
        Spinner hospitalSpinner = findViewById(R.id.hospital_spinner);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalSpinner.setAdapter(spinnerAdapter);

        // 위치 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

        hospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Hospital selectedHospital = filteredHospitalList.get(position);;
                double userLatitude = userLocation.getLatitude();
                double userLongitude = userLocation.getLongitude();
                float distance = selectedHospital.calculateDistance(userLatitude, userLongitude);


                Intent intent = new Intent(HospitalActivity.this, HospitalPageActivity.class);
                intent.putExtra("latitude", selectedHospital.getLatitude());
                intent.putExtra("longitude", selectedHospital.getLongitude());
                intent.putExtra("HOSPITAL_NAME", selectedHospital.getName());
                intent.putExtra("HOSPITAL_ADDRESS", selectedHospital.getAddress());
                intent.putExtra("HOSPITAL_DISTANCE", distance);
                intent.putExtra("HOSPITAL_RATING", selectedHospital.getAverageRating());
                startActivity(intent);
            }
        });

        hospitalSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHospitalList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_options,
                R.layout.hospital_spinner_item // 커스텀 아이템 레이아웃
        );
        spinnerAdapter.setDropDownViewResource(R.layout.hospital_spinner_dropdown_item); // DropDown 레이아웃 설정
        hospitalSpinner.setAdapter(spinnerAdapter);

        hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 정렬 기준 처리
                switch (position) {
                    case 0:
                        sortHospitalListByName();
                        break;
                    case 1:
                        sortHospitalListByDistance();
                        break;
                    case 2:
                        sortHospitalListByRating();
                        break;
                }
                adapter.updateList(filteredHospitalList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택 안 된 경우 처리하지 않음
            }
        });

        fetchNearbyHospitals();
    }

    private void filterHospitalList(String query) {
        filteredHospitalList.clear();
        if (query.isEmpty()) {
            filteredHospitalList.addAll(hospitalList); // 검색어가 없으면 전체 리스트
        } else {
            for (Hospital hospital : hospitalList) {
                if (hospital.getName().toLowerCase().contains(query.toLowerCase()) ||
                        hospital.getAddress().toLowerCase().contains(query.toLowerCase())) {
                    filteredHospitalList.add(hospital);
                }
            }
        }
        adapter.notifyDataSetChanged(); // 필터링 결과 어댑터 갱신
    }

    private void sortHospitalListByName() { // 가나다
        Collections.sort(filteredHospitalList, new Comparator<Hospital>() {
            @Override
            public int compare(Hospital h1, Hospital h2) {
                return h1.getName().compareToIgnoreCase(h2.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }


    private void sortHospitalListByDistance() { // 거리순
        if (userLocation == null) { // 거리 널 방지
            Log.e("HospitalActivity", "사용자 위치가 없습니다.");
            return;
        }
        Collections.sort(filteredHospitalList, new Comparator<Hospital>() {
            @Override
            public int compare(Hospital h1, Hospital h2) {
                return Float.compare(h1.getDistance(), h2.getDistance());
            }
        });
        adapter.notifyDataSetChanged();
    }


    private void sortHospitalListByRating() { // 별점순
        Collections.sort(filteredHospitalList, new Comparator<Hospital>() {
            @Override
            public int compare(Hospital h1, Hospital h2) {
                return Float.compare(h2.getAverageRating(), h1.getAverageRating());
            }
        });
        adapter.notifyDataSetChanged();
    }


    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            userLocation = location;
                            fetchNearbyHospitals();
                        } else {
                            Log.e("HospitalActivity", "현재 위치를 가져올 수 없습니다.");
                        }
                    });
        }
    }

    private void fetchNearbyHospitals() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("HospitalActivity", "위치 권한이 없습니다.");
        }

        hospitalList.clear();
        hospitalList.add(new Hospital("서울 동물병원", "서울특별시 강남구", 37.497946, 127.027619));
        hospitalList.add(new Hospital("서울종합동물병원", "서울특별시 성북구 보문로 137 성균빌딩", 37.5833, 127.0025));
        hospitalList.add(new Hospital("이안동물의학센터", "서울특별시 강남구 선릉로 806 킹콩빌딩 5층", 37.5273, 127.0405));
        hospitalList.add(new Hospital("도그마루 동물병원", "서울특별시 송파구 석촌호수로 104 1층", 37.5051, 127.0875));
        hospitalList.add(new Hospital("OK동물병원", "서울특별시 강남구 도곡로51길 4 1층", 37.5000, 127.0500));
        hospitalList.add(new Hospital("윤신근동물병원", "서울특별시 중구 퇴계로 226", 37.5600, 126.9900));
        hospitalList.add(new Hospital("VIP 동물의료센터 성북점", "서울특별시 성북구 동소문로 73 시티 플레이스 2F", 37.5900, 127.0200));
        hospitalList.add(new Hospital("서울탑동물병원", "서울특별시 양천구 목동동로 293", 37.5300, 126.8700));
        hospitalList.add(new Hospital("부산베스트동물의료센터", "부산광역시 사하구 장평로 129", 35.0975, 128.9667));
        hospitalList.add(new Hospital("동래동물의료센터", "부산광역시 동래구 명륜로 12", 35.2050, 129.0833));
        hospitalList.add(new Hospital("해운대24시동물의료센터", "부산광역시 해운대구 중동 1378-7", 35.1633, 129.1633));
        hospitalList.add(new Hospital("동물메디컬24시부산점", "부산광역시 해운대구 좌동로 50", 35.1700, 129.1742));
        hospitalList.add(new Hospital("휴동물의료센터", "부산광역시 해운대구 우동 1474 해운대아이파크 상가 B동", 35.1639, 129.1639));
        hospitalList.add(new Hospital("조양래동물의료센터", "부산광역시 해운대구 석대천로 23번길 6", 35.1892, 129.1286));
        hospitalList.add(new Hospital("해운대동물병원", "부산광역시 해운대구 송정동 48-1", 35.1800, 129.1992));
        hospitalList.add(new Hospital("해운대펫동물병원", "부산광역시 해운대구 중동1로 37번길 7", 35.1633, 129.1633));
        hospitalList.add(new Hospital("BS조은동물병원", "부산광역시 해운대구 윗반송로 73", 35.2292, 129.1319));
        hospitalList.add(new Hospital("카이저동물병원", "부산광역시 북구 금곡대로 182", 35.2100, 129.0300));
        hospitalList.add(new Hospital("해운대 동물병원", "부산광역시 해운대구", 35.163097, 129.163573));
        hospitalList.add(new Hospital("창원 동물병원", "창원시 중앙동", 35.2428646, 128.6505396));
        hospitalList.add(new Hospital("창원메디컬동물병원", "경상남도 창원시 성산구 마디미로73번길 25 신우주차빌딩 1층", 35.2275, 128.6811));
        hospitalList.add(new Hospital("아이처럼동물병원", "경상남도 창원시 성산구 용지로133번길 5 키다리빌딩 1층 102호", 35.2238, 128.6825));
        hospitalList.add(new Hospital("우리동물병원", "경상남도 창원시 의창구 팔용로 425 화성빌딩", 35.2542, 128.6236));
        hospitalList.add(new Hospital("한일동물병원", "경상남도 창원시 마산회원구 양덕로 170", 35.2345, 128.5832));
        hospitalList.add(new Hospital("스카이동물병원", "경상남도 창원시 성산구 동산로 122 스카이웰빙파크 2층", 35.2290, 128.6815));
        hospitalList.add(new Hospital("탑동물병원", "경상남도 창원시 의창구 용동로 85 한마음빌딩 304호", 35.2468, 128.6259));
        hospitalList.add(new Hospital("조항일동물병원", "경상남도 창원시 진해구 석동 543-2", 35.1462, 128.7023));

        if (userLocation != null) {
            for (Hospital hospital : hospitalList) {
                float[] results = new float[1];
                Location.distanceBetween(
                        userLocation.getLatitude(),
                        userLocation.getLongitude(),
                        hospital.getLatitude(),
                        hospital.getLongitude(),
                        results
                );
                hospital.setDistance(results[0]);
            }
        }

        filteredHospitalList.clear();
        filteredHospitalList.addAll(hospitalList);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            String hospitalName = data.getStringExtra("HOSPITAL_NAME");
            String reviewText = data.getStringExtra("REVIEW_TEXT");
            float reviewRating = data.getFloatExtra("REVIEW_RATING", 0);
            String reviewDate = data.getStringExtra("REVIEW_DATE");
            String userId = data.getStringExtra("REVIEW_ID");

            // 병원 이름을 기준으로 해당 병원 객체를 찾음
            for (Hospital hospital : hospitalList) {
                if (hospital.getName().equals(hospitalName)) {
                    // 리뷰 추가 및 평균 별점 업데이트
                    HospitalReviewItem newReview = new HospitalReviewItem(userId, reviewText, reviewRating, reviewDate);
                    hospital.addReview(newReview);

                    // 병원 리스트 어댑터 갱신
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }


}
