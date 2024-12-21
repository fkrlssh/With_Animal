package kr.ac.changwon.wa_ui_design;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

        // 돌아가기 버튼
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
                    LostPet lostPet = new LostPet();
                    lostPet.setName("이름");  // 임시 데이터
                    lostPet.setSpecies("종");
                    lostPet.setLost_date(date);
                    lostPet.setLost_location(place);
                    lostPet.setDescription(description);
                    lostPet.setPhoto_url(photoUri);

                    Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
                    ApiService apiService = retrofit.create(ApiService.class);
                    Call<Void> call = apiService.registerLostPet(lostPet);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(MissingWriteActivity.this, "등록 성공!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(MissingWriteActivity.this, "등록 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MissingWriteActivity.this, "서버 오류 발생", Toast.LENGTH_SHORT).show();
                        }
                    });
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
            setTodayAsDefaultDate();
        }, year, month, day);

        datePickerDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra("address");
            missingWritePlace.setText(address);
        } else if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            if (selectedImage != null) {
                missingWritePetphoto.setImageURI(selectedImage);
                uploadPhoto(selectedImage);  // 이미지 업로드 메소드 호출
            } else {
                Toast.makeText(this, "사진을 선택하지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadPhoto(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesRead;
            byte[] data = new byte[1024];
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            byte[] byteArray = buffer.toByteArray();
            buffer.close();
            inputStream.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), byteArray);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", "upload.jpg", requestFile);
            RequestBody description = RequestBody.create(MultipartBody.FORM, "실종 동물 사진");

            Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<ResponseBody> call = apiService.uploadPhoto(body, description);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String uploadedImageUrl = response.body().string();
                            missingWritePetphoto.setImageURI(imageUri);
                            missingWritePetphoto.setTag(uploadedImageUrl);
                            if (!isFinishing()) {
                                Toast.makeText(getApplicationContext(), "사진 업로드 성공", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!isFinishing()) {
                            Toast.makeText(getApplicationContext(), "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (!isFinishing()) {
                        Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "사진 업로드 중 오류 발생", Toast.LENGTH_SHORT).show();
        }
    }
}