package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class HospitalActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private ListView hospitalListView;
    private List<Hospital> hospitalList;
    private HospitalListAdapter adapter;
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;

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
        adapter = new HospitalListAdapter(this, hospitalList);
        hospitalListView.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 위치 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

        hospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Hospital selectedHospital = hospitalList.get(position);
                double userLatitude = userLocation.getLatitude();
                double userLongitude = userLocation.getLongitude();
                float distance = selectedHospital.calculateDistance(userLatitude, userLongitude);

                Intent intent = new Intent(HospitalActivity.this, HospitalPageActivity.class);
                intent.putExtra("latitude", selectedHospital.getLatitude());
                intent.putExtra("longitude", selectedHospital.getLongitude());
                intent.putExtra("HOSPITAL_NAME", selectedHospital.getName());
                intent.putExtra("HOSPITAL_ADDRESS", selectedHospital.getAddress());
                intent.putExtra("HOSPITAL_DISTANCE", distance);
                startActivity(intent);
            }
        });

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
        hospitalList.add(new Hospital("해운대 동물병원", "부산광역시 해운대구", 35.163097, 129.163573));
        hospitalList.add(new Hospital("창원 동물병원", "창원시 중앙동", 35.2428646, 128.6505396));

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
                hospital.setDistance(results[0]); // 거리 정보 저장
            }
        }

        adapter.notifyDataSetChanged();
    }

}
