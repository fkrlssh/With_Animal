package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
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

public class PetRegisterActivity extends AppCompatActivity {
    private String photoPath = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_pet_register);

        ImageButton PetRegisterReturnHomeButton = findViewById(R.id.register_return_home);
        PetRegisterReturnHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(PetRegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // 동물 사진 추가 부분
        Button petPhotoButton = findViewById(R.id.register_photoB);
        petPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
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

                if (petAge.isEmpty()) {
                    petAgeEditText.setError("나이를 입력하세요");
                    isValid = false;
                }

                if (selectedGenderId == -1) {
                    // 성별 선택 오류 메시지 추가
                    Toast.makeText(PetRegisterActivity.this, "성별을 선택하세요", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (isValid) {
                    String gender;
                    if(selectedGenderId == R.id.register_male){
                        gender = "남";
                    }
                    else{
                        gender = "여";
                    }

                    Intent intent = new Intent(PetRegisterActivity.this, MainActivity.class);
                    intent.putExtra("petName", petName);
                    intent.putExtra("petSpecies", petSpecies);
                    intent.putExtra("petAge", petAge);
                    intent.putExtra("gender", gender);
                    intent.putExtra("photoPath", photoPath);

                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            // 갤러리 또는 포토에서 선택한 URI 가져오기
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // URI를 파일 경로로 변환
                String filePath = getRealPathFromURI(selectedImageUri);
                if (filePath != null) {
                    photoPath = filePath;
                    Toast.makeText(this, "사진이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    // 등록 페이지의 ImageView 업데이트
                    ImageView petPhoto = findViewById(R.id.register_petphoto);
                    petPhoto.setImageURI(Uri.parse(photoPath));
                } else {
                    Toast.makeText(this, "사진 경로를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // URI를 파일 경로로 변환하는 메서드
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

}
