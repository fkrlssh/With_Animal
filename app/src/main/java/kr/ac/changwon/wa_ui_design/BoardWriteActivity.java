package kr.ac.changwon.wa_ui_design;


import android.util.Log;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;

public class BoardWriteActivity extends AppCompatActivity {
    private EditText boardWriteTitle, boardWriteText;
    private OkHttpClient client = new OkHttpClient();
    private String jwtToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        jwtToken = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("token", null);  // 'token' 키 사용 (LoginActivity와 통일)

        boardWriteTitle = findViewById(R.id.board_write_title);
        boardWriteText = findViewById(R.id.board_write_text);

        ImageButton boardWriteReturnBoard = findViewById(R.id.board_write_return_board);
        boardWriteReturnBoard.setOnClickListener(view -> finish());

        Button boardWriteRegisterB = findViewById(R.id.board_wrtie_registerB);
        boardWriteRegisterB.setOnClickListener(v -> {
            String boardTitle = boardWriteTitle.getText().toString();
            String boardText = boardWriteText.getText().toString();

            if (boardTitle.isEmpty()) {
                boardWriteTitle.setError("제목을 입력해주세요.");
                boardWriteTitle.requestFocus();
                return;
            }
            if (boardText.isEmpty()) {
                boardWriteText.setError("내용을 입력해주세요.");
                boardWriteText.requestFocus();
                return;
            }

            // 현재 날짜 추가
            String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

            // 기존 인텐트 기반 로직
            Intent intent = new Intent();
            intent.putExtra("boardTitle", boardTitle);
            intent.putExtra("boardText", boardText);
            intent.putExtra("boardDate", currentDate);
            intent.putExtra("isGeneralBoard", true);
            setResult(RESULT_OK, intent);

            // 서버로 게시글 전송
            if (jwtToken != null) {
                sendPostToServer(boardTitle, boardText);
            } else {
                Toast.makeText(BoardWriteActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    // 게시글 서버 전송 메서드
    private void sendPostToServer(String title, String content) {
        String json = "{"
                + "\"title\":\"" + title + "\","
                + "\"content\":\"" + content + "\","
                + "\"photo_url\":\"http://example.com/sample.jpg\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://<ngrok-url>/board/posts")  // Flask 서버 URL
                .addHeader("Authorization", "Bearer " + jwtToken)  // JWT 토큰 추가
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 서버 연결 실패 로그 추가
                Log.e("BoardWriteActivity", "서버 연결 실패: " + e.getMessage());
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(BoardWriteActivity.this, "서버 연결 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 게시글 전송 성공 로그
                    Log.d("BoardWriteActivity", "게시글 서버 전송 성공!");
                    runOnUiThread(() ->
                            Toast.makeText(BoardWriteActivity.this, "게시글 서버 전송 성공!", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    // 서버 응답 오류 로그
                    Log.e("BoardWriteActivity", "서버 응답 오류: " + response.message());
                    runOnUiThread(() ->
                            Toast.makeText(BoardWriteActivity.this, "서버 오류: " + response.message(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
