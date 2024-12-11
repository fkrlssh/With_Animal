package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetRegisterActivity extends AppCompatActivity {
    private String photoPath = null; // 선택한 사진 경로를 저장하는 변수
    private String userId = null; // 로그인된 사용자 ID

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_pet_register);

        // SharedPreferences에서 user_id 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            // 로그인 화면으로 이동
            Intent intent = new Intent(PetRegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) // Flask 서버 URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위한 Gson 사용
                .build();

        // ApiService 초기화
        ApiService apiService = retrofit.create(ApiService.class);

        // 뒤로 가기 버튼 이벤트 설정
        ImageButton PetRegisterReturnHomeButton = findViewById(R.id.register_return_home);
        PetRegisterReturnHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(PetRegisterActivity.this, MainActivity.class);
            startActivity(intent); // 메인 화면으로 이동
            finish(); // 현재 액티비티 종료
        });

        // 사진 추가 버튼 이벤트 설정
        Button petPhotoButton = findViewById(R.id.register_photoB);
        petPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 갤러리에서 이미지 선택
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); // 이미지 타입 필터링
                startActivityForResult(intent, 101); // 101은 요청 코드
            }
        });

        // 등록 버튼 이벤트 설정
        Button registerButton = findViewById(R.id.registerB);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자 입력 값 가져오기
                EditText petNameEditText = findViewById(R.id.register_petname);
                EditText petSpeciesEditText = findViewById(R.id.register_petspecies);
                EditText petAgeEditText = findViewById(R.id.register_petage);
                RadioGroup genderGroup = findViewById(R.id.register_RG);

                String petName = petNameEditText.getText().toString().trim();
                String petSpecies = petSpeciesEditText.getText().toString().trim();
                String petAge = petAgeEditText.getText().toString().trim();
                int selectedGenderId = genderGroup.getCheckedRadioButtonId();

                // 입력 값 검증
                boolean isValid = true;

                if (petName.isEmpty()) {
                    petNameEditText.setError("이름을 입력하세요");
                    isValid = false;
                }

                if (petSpecies.isEmpty()) {
                    petSpeciesEditText.setError("종을 입력하세요");
                    isValid = false;
                }

                if (petAge.isEmpty()) {
                    petAgeEditText.setError("나이를 입력하세요");
                    isValid = false;
                }

                if (selectedGenderId == -1) {
                    Toast.makeText(PetRegisterActivity.this, "성별을 선택하세요", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (isValid) {
                    // 성별 값 설정
                    String gender = (selectedGenderId == R.id.register_male) ? "남" : "여";

                    // 서버에 등록 요청
                    registerPetOnServer(userId, petName, petSpecies, petAge, gender, photoPath);
                }
            }
        });
    }

    private boolean isValidAge(String age) { // ~살 안되게
        return age.matches("^[0-9]+$");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                String filePath = getRealPathFromURI(selectedImageUri);
                if (filePath != null) {
                    photoPath = filePath; // 선택한 사진 경로 저장
                    Toast.makeText(this, "사진이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    // 선택한 사진을 ImageView에 표시
                    ImageView petPhoto = findViewById(R.id.register_petphoto);
                    petPhoto.setImageURI(Uri.parse(photoPath));
                } else {
                    Toast.makeText(this, "사진 경로를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // URI를 실제 파일 경로로 변환
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {android.provider.MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    // 서버에 동물 등록 요청
    private void registerPetOnServer(String userId, String petName, String petSpecies, String petAge, String gender, String photoPath) {
        Pet pet = new Pet(userId, petName, petSpecies, petAge, gender, photoPath);

        // Retrofit을 통해 API 호출
        ApiService apiService = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) // Flask 서버 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        Call<Void> call = apiService.registerPet(pet);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "등록 성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "등록 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
