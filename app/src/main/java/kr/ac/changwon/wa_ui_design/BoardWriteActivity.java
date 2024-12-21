package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

public class BoardWriteActivity extends AppCompatActivity {
    private RadioGroup boardWriteRadioGroup;
    private RadioButton boardWriteQuestion;
    private RadioButton boardWriteTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        ImageButton boardWriteReturnBoard = findViewById(R.id.board_write_return_board); // 글쓰기 창 나가기
        boardWriteReturnBoard.setOnClickListener(view -> {
            finish();
        });

        EditText boardWriteTitle = findViewById(R.id.board_write_title);
        EditText boardWriteText = findViewById(R.id.board_write_text);

        Button boardWriteRegisterB = findViewById(R.id.board_wrtie_registerB);
        boardWriteRegisterB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String boardTitle = boardWriteTitle.getText().toString();
                String boardText = boardWriteText.getText().toString();

                if (boardTitle.isEmpty()) {
                    boardWriteTitle.setClickable(false);
                    boardWriteTitle.setError("제목을 입력해주세요.");
                    boardWriteTitle.requestFocus(); // 제목 입력창으로 포커스 이동
                    return;
                }
                if (boardText.isEmpty()) {
                    boardWriteTitle.setClickable(false);
                    boardWriteText.setError("내용을 입력해주세요.");
                    boardWriteText.requestFocus(); // 내용 입력창으로 포커스 이동
                    return;
                }

                String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date()); // 등록 날짜

                Intent intent = new Intent();
                intent.putExtra("boardTitle", boardTitle);
                intent.putExtra("boardText", boardText);
                intent.putExtra("boardDate", currentDate);

                intent.putExtra("isGeneralBoard", true);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}