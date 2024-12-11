package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardWriteActivity extends AppCompatActivity {
    private RadioGroup boardWriteRadioGroup;
    private RadioButton boardWriteQuestion;
    private RadioButton boardWriteTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        ImageButton boardWriteReturnBoard = findViewById(R.id.board_write_return_board); // 글쓰기 창 나가기
        boardWriteReturnBoard.setOnClickListener(view -> finish());

        EditText boardWriteTitle = findViewById(R.id.board_write_title);
        EditText boardWriteText = findViewById(R.id.board_write_text);
        boardWriteRadioGroup = findViewById(R.id.boardwrite_RG);
        boardWriteQuestion = findViewById(R.id.board_write_question);
        boardWriteTip = findViewById(R.id.board_wrtie_tip);

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

            // 선택된 카테고리에 따라 category_id 설정
            int categoryId = boardWriteRadioGroup.getCheckedRadioButtonId() == boardWriteQuestion.getId() ? 1 : 2;

            // 사진 URL 기본값 설정
            String photoUrl = "http://example.com/default_photo.jpg"; // 기본 사진 URL

            // BoardPost 객체 생성
            BoardPost boardPost = new BoardPost(categoryId, boardTitle, boardText, photoUrl);

            // SharedPreferences에서 토큰 가져오기
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            Log.d("BoardWriteActivity", "토큰 확인: " + token);

            if (token == null) {
                Toast.makeText(BoardWriteActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrofit 클라이언트 초기화
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + token);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            // 게시물 등록 API 호출
            // 게시물 등록 API 호출
            ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
            Call<Void> call = apiService.createBoardPost(boardPost);

// 로그 추가
            Log.d("BoardWriteActivity", "Request URL: " + Constants.BASE_URL);
            Log.d("BoardWriteActivity", "Request Headers: Authorization Bearer " + RetrofitClientInstance.getAuthToken()); // authToken 가져오는 메서드 추가 필요
            Log.d("BoardWriteActivity", "Request Body: " + boardPost.toString());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(BoardWriteActivity.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("boardTitle", boardTitle);
                        resultIntent.putExtra("boardText", boardText);
                        resultIntent.putExtra("boardDate", new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date()));
                        setResult(RESULT_OK, resultIntent);

                        finish(); // 액티비티 종료
                    } else {
                        Toast.makeText(BoardWriteActivity.this, "게시물 등록 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e("BoardWriteActivity", "Error Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(BoardWriteActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("BoardWriteActivity", "Error: " + t.getMessage());
                }
            });
        });
    }
}
