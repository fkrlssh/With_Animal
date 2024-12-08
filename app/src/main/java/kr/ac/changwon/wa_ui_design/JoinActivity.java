package kr.ac.changwon.wa_ui_design;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join); // join.xml 연결

        // 입력 필드 참조
        EditText userIdField = findViewById(R.id.join_id);
        EditText userNameField = findViewById(R.id.join_name);
        EditText emailField = findViewById(R.id.join_email);
        EditText passwordField = findViewById(R.id.join_password);
        EditText phoneField = findViewById(R.id.join_tel);
        Button joinButton = findViewById(R.id.joinButton);

        // Retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) // 서버 주소 재사용
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // 회원가입 버튼 클릭 이벤트
        joinButton.setOnClickListener(v -> {
            // 입력된 데이터 가져오기
            String userId = userIdField.getText().toString();
            String userName = userNameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String phone = phoneField.getText().toString();

            // User 객체 생성
            User user = new User(userId, userName, email, password, phone);

            // 서버로 데이터 전송
            apiService.registerUser(user).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(JoinActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        finish(); // 회원가입 성공 후 화면 종료
                    } else {
                        Toast.makeText(JoinActivity.this, "회원가입 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(JoinActivity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("JoinActivity", "Error: ", t);
                }
            });
        });
    }
}
