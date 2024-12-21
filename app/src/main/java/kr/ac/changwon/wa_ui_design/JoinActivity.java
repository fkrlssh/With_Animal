package kr.ac.changwon.wa_ui_design;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
        setContentView(R.layout.join);

        ImageButton joinReturnLogin = findViewById(R.id.join_return_login);
        joinReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText userIdField = findViewById(R.id.join_id);
        EditText userNameField = findViewById(R.id.join_name);
        EditText emailField = findViewById(R.id.join_email);
        EditText passwordField = findViewById(R.id.join_password);
        EditText phoneField = findViewById(R.id.join_tel);
        Button joinButton = findViewById(R.id.joinButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        joinButton.setOnClickListener(v -> {
            String userId = userIdField.getText().toString();
            String userName = userNameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String phone = phoneField.getText().toString();

            User user = new User(userId, userName, email, password, phone);

            apiService.registerUser(user).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(JoinActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
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
