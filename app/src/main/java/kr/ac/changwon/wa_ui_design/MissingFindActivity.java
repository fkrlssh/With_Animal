package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MissingFindActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_MAP = 1;
    private static final int REQUEST_CODE_PHOTO = 2;

    private TextView missingFindPlace;
    private ImageView missingFindPetphoto;
    private TextView missingFindTel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_find);

        missingFindPetphoto = findViewById(R.id.missing_find_petphoto);
        missingFindPlace = findViewById(R.id.missing_find_place);
        missingFindTel = findViewById(R.id.missing_find_tel);

        Intent intent = getIntent(); // ui상으로 입력한 정보 보이게
        if (intent != null) {
            String tel = intent.getStringExtra("tel");
            String address = intent.getStringExtra("address");
            String photoUri = intent.getStringExtra("photoUri");

            if (tel != null) {
                missingFindTel.setText(tel);
            }

            if (address != null) {
                missingFindPlace.setText(address);
            }

            if (photoUri != null) {
                Uri photo = Uri.parse(photoUri);
                missingFindPetphoto.setImageURI(photo);
                missingFindPetphoto.setTag(photoUri);
            }
        }

        missingFindPetphoto = findViewById(R.id.missing_find_petphoto);
        ImageButton photoAddButton = findViewById(R.id.missing_find_photoadd);
        photoAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoIntent, REQUEST_CODE_PHOTO);
            }
        });

        missingFindPlace = findViewById(R.id.missing_find_place);
        ImageButton placeButton = findViewById(R.id.missing_find_placeButton);
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(MissingFindActivity.this, MissingMapActivity.class);
                startActivityForResult(mapIntent, REQUEST_CODE_MAP);
            }
        });

        findViewById(R.id.missing_find_registerB).setOnClickListener(view -> {
            String place = missingFindPlace.getText().toString().trim();
            String tel = missingFindTel.getText().toString().trim();
            String photoUri = missingFindPetphoto.getTag() != null ? missingFindPetphoto.getTag().toString() : null;

            boolean hasError = false;

            if (place.isEmpty()) {
                missingFindPlace.setError("발견 장소를 입력하세요.");
                hasError = true;
            }

            if (!isValidPhoneNumber(tel)) {
                missingFindTel.setError("알맞지 않은 전화번호 형식입니다.");
                hasError = true;
            }

            if (photoUri == null) {
                missingFindPetphoto.setContentDescription("사진을 등록하세요.");
                hasError = true;
            }

            if (!hasError) {
                finish();
            }
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^[0-9]{11}$");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra("address");
            missingFindPlace.setText(address);
        }
        else if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                missingFindPetphoto.setImageURI(selectedImage);
                missingFindPetphoto.setTag(selectedImage.toString());
            } else {
                missingFindPetphoto.setContentDescription("사진을 선택하지 못했습니다.");
            }
        }
    }
}
