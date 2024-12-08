package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BoardTipActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_tip);

        ImageButton tipReturnBoard = findViewById(R.id.tip_return_board);
        tipReturnBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardTipActivity.this, MainActivity.class);
                intent.putExtra("fragmentToLoad", "boardFragment"); // 전체 게시판 화면으로 나오기 위해선 필요
                startActivity(intent);
                finish();
            }
        });
    }
}
