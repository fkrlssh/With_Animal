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

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText loginIdField, loginPasswordField;
    private Button loginButton;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginIdField = findViewById(R.id.login_id);
        loginPasswordField = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.loginButton);

        Button joinButton = findViewById(R.id.login_joinButton);
        joinButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(intent);
        });

        apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);

        loginButton.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {

        String userId = loginIdField.getText().toString().trim();
        String password = loginPasswordField.getText().toString().trim();

        if (userId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(userId, password);
        apiService.loginUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("LoginActivity", "응답 코드: " + response.code());
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("LoginActivity", "응답 데이터: " + responseBody);

                        JSONObject jsonObject = new JSONObject(responseBody);

                        // 최상위 token 가져오기
                        String token = jsonObject.optString("token", null);

                        if (token != null) {
                            RetrofitClientInstance.setAuthToken(token);
                            saveAuthToken(token);
                            Log.d("LoginActivity", "토큰 저장 완료. MainActivity 이동 준비.");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("LoginActivity", "MainActivity로 이동 완료.");
                        }
                        else {
                            Log.e("LoginActivity", "토큰이 응답에 포함되지 않았습니다.");
                            Toast.makeText(LoginActivity.this, "토큰을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("LoginActivity", "JSON 파싱 오류: " + e.getMessage());
                        Toast.makeText(LoginActivity.this, "로그인 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.e("LoginActivity", "로그인 실패: " + response.message());
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LoginActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "서버 연결 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();

        Log.d("LoginActivity", "토큰 저장 완료: " + token);
    }
}
