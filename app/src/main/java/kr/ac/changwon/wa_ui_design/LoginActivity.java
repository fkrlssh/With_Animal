package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText loginIdField, loginPasswordField; // 사용자 ID와 비밀번호 입력 필드
    private Button loginButton; // 로그인 버튼

    private ApiService apiService; // Retrofit API 인터페이스

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // 로그인 화면 레이아웃 설정

        // UI 요소 초기화
        loginIdField = findViewById(R.id.login_id);
        loginPasswordField = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.loginButton);

        // 회원가입 버튼 설정
        Button joinButton = findViewById(R.id.login_joinButton);
        joinButton.setOnClickListener(v -> {
            // 회원가입 화면으로 이동
            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(intent);
        });

        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) // Flask 서버 URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON 변환기
                .build();

        // ApiService 생성
        apiService = retrofit.create(ApiService.class);

        // 로그인 버튼 클릭 이벤트 설정
        loginButton.setOnClickListener(v -> performLogin());
    }

    /**
     * 사용자가 입력한 ID와 비밀번호를 이용해 로그인 요청 수행
     */
    private void performLogin() {
        // 입력값 가져오기
        String userId = loginIdField.getText().toString().trim();
        String password = loginPasswordField.getText().toString().trim();

        // 입력값 유효성 검사
        if (userId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // User 객체 생성
        User user = new User(userId, password);

        // 로그인 API 호출
        apiService.loginUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 로그인 성공
                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                    // SharedPreferences에 user_id 저장
                    saveUserId(userId);

                    // MainActivity로 이동
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 로그인 실패
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 서버 또는 네트워크 오류
                Toast.makeText(LoginActivity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * SharedPreferences에 user_id 저장
     */
    private void saveUserId(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", userId); // user_id 저장
        editor.apply();
        Log.d("LoginActivity", "User ID 저장 완료: " + userId);
    }
}
