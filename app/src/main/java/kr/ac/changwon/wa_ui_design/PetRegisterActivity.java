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
    private String photoPath = null;
    private String userId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_pet_register);


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PetRegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiService apiService = retrofit.create(ApiService.class);


        ImageButton PetRegisterReturnHomeButton = findViewById(R.id.register_return_home);
        PetRegisterReturnHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(PetRegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        Button petPhotoButton = findViewById(R.id.register_photoB);
        petPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 101); // 101은 요청 코드
            }
        });

        Button registerButton = findViewById(R.id.registerB);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText petNameEditText = findViewById(R.id.register_petname);
                EditText petSpeciesEditText = findViewById(R.id.register_petspecies);
                EditText petAgeEditText = findViewById(R.id.register_petage);
                RadioGroup genderGroup = findViewById(R.id.register_RG);

                String petName = petNameEditText.getText().toString().trim();
                String petSpecies = petSpeciesEditText.getText().toString().trim();
                String petAge = petAgeEditText.getText().toString().trim();
                int selectedGenderId = genderGroup.getCheckedRadioButtonId();

                boolean isValid = true;

                if (petName.isEmpty()) {
                    petNameEditText.setError("이름을 입력하세요");
                    isValid = false;
                }

                if (petSpecies.isEmpty()) {
                    petSpeciesEditText.setError("종을 입력하세요");
                    isValid = false;
                }
                else if (!isValidAge(petAge)) { // 나이 유효성 검사
                    petAgeEditText.setError("나이는 숫자만 입력 가능합니다.");
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
                    String gender = (selectedGenderId == R.id.register_male) ? "남" : "여";
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
                    photoPath = filePath;
                    Toast.makeText(this, "사진이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    ImageView petPhoto = findViewById(R.id.register_petphoto);
                    petPhoto.setImageURI(Uri.parse(photoPath));
                }
                else {
                    Toast.makeText(this, "사진 경로를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

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


    private void registerPetOnServer(String userId, String petName, String petSpecies, String petAge, String gender, String photoPath) {
        Pet pet = new Pet(userId, petName, petSpecies, petAge, gender, photoPath);


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

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("petName", petName);
                    resultIntent.putExtra("petSpecies", petSpecies);
                    resultIntent.putExtra("petAge", petAge);
                    resultIntent.putExtra("gender", gender);
                    resultIntent.putExtra("photoPath", photoPath);
                    setResult(RESULT_OK, resultIntent);

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
