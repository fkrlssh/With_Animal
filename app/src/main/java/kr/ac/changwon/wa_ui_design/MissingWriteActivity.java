package kr.ac.changwon.wa_ui_design;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MissingWriteActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MAP = 1;
    private static final int REQUEST_CODE_PHOTO = 2;

    private TextView missingWritePlace;
    private TextView missingWriteDate;
    private EditText missingWriteChar;
    private ImageView missingWritePetphoto;
    private Calendar selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_write);

        ImageButton missingWriteReturn = findViewById(R.id.missing_write_return_missing);
        missingWriteReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MissingWriteActivity.this, MissingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        missingWritePetphoto = findViewById(R.id.missing_write_petphoto);
        ImageButton photoAddButton = findViewById(R.id.missing_write_photoadd);
        photoAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoIntent, REQUEST_CODE_PHOTO);
            }
        });

        // 실종 장소
        missingWritePlace = findViewById(R.id.missing_write_place);
        ImageButton placeButton = findViewById(R.id.missing_write_placeButton);
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(MissingWriteActivity.this, MissingMapActivity.class);
                startActivityForResult(mapIntent, REQUEST_CODE_MAP);
            }
        });

        missingWriteDate = findViewById(R.id.missing_write_date);
        selectedDate = Calendar.getInstance();
        setTodayAsDefaultDate();
        ImageButton dateButton = findViewById(R.id.missing_write_dateButton);
        dateButton.setOnClickListener(view -> showDatePickerDialog());

        missingWriteChar = findViewById(R.id.missing_write_char);
        missingWriteChar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 120) { //글자제한
                    missingWriteChar.setText(s.subSequence(0, 120));
                    missingWriteChar.setSelection(120);
                    Toast.makeText(MissingWriteActivity.this, "특징은 최대 120자까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button missingWriteRegisterB = findViewById(R.id.missing_write_registerB);
        missingWriteRegisterB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = missingWriteDate.getText().toString().trim();
                String place = missingWritePlace.getText().toString().trim();
                String description = missingWriteChar.getText().toString().trim();
                String photoUri = missingWritePetphoto.getTag() != null ? missingWritePetphoto.getTag().toString() : null;

                boolean hasError = false;

                if (place.isEmpty()) {
                    missingWritePlace.setError("실종 장소를 입력하세요.");
                    hasError = true;
                }

                if (description.isEmpty()) {
                    missingWriteChar.setError("특징을 입력하세요.");
                    hasError = true;
                }

                if (photoUri == null) {
                    Toast.makeText(MissingWriteActivity.this, "사진을 등록하세요.", Toast.LENGTH_SHORT).show();
                    hasError = true;
                }

                if (!hasError) {
                    String name = "이름";
                    String species = "종";
                    MissingData missingData = new MissingData(name, species, 0, "미상", place, date, description, photoUri);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newData", missingData);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    private void setTodayAsDefaultDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        missingWriteDate.setText(dateFormat.format(selectedDate.getTime()));
    }

    private void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerStyle, (view, year1, month1, dayOfMonth) -> {
            selectedDate.set(year1, month1, dayOfMonth);
            setTodayAsDefaultDate(); // 선택한 날짜 반영
        }, year, month, day);

        datePickerDialog.setOnShowListener(dialog -> {
            Button positiveButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.black));
            Button negativeButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE);
            negativeButton.setTextColor(getResources().getColor(R.color.black));
        });

        datePickerDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra("address");
            missingWritePlace.setText(address);
        }
        else if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                missingWritePetphoto.setImageURI(selectedImage);
                missingWritePetphoto.setTag(selectedImage.toString());
            }
            else {
                Toast.makeText(this, "이미지를 선택하지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
