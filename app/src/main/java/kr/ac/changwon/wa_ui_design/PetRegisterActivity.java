package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PetRegisterActivity extends AppCompatActivity {
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
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
