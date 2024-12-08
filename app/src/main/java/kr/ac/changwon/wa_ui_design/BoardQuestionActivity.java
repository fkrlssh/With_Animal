package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BoardQuestionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_question);

        ImageButton questionReturnBoard = findViewById(R.id.question_return_board);
        questionReturnBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardQuestionActivity.this, MainActivity.class);
                intent.putExtra("fragmentToLoad", "boardFragment"); // 전체 게시판 화면으로 나오기 위해선 필요
                startActivity(intent);
                finish();
            }
        });
    }

// 디비로 정보 불러와서 ui 보이도록 해야 함
}
