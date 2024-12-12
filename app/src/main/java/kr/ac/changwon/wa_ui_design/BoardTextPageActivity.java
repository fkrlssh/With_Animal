package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BoardTextPageActivity extends AppCompatActivity {
    private ArrayList<Comment> commentList; // 댓글 데이터 저장 리스트
    private CommentAdapter commentAdapter;
    private RecyclerView commentRecyclerView;
    private EditText boardTextpageComment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_textpage);

        // 글 데이터
        TextView boardTitle = findViewById(R.id.board_textpage_title);
        TextView boardText = findViewById(R.id.board_textpage_text);
        TextView boardDate = findViewById(R.id.board_textpage_date);

        TextView boardCategory = findViewById(R.id.board_category);
        boolean isQuestionBoard = getIntent().getBooleanExtra("isQuestionBoard", false);
        boolean isTipBoard = getIntent().getBooleanExtra("isTipBoard", false);

        boardCategory.setText(isQuestionBoard ? "질문" : "꿀팁");

        Intent intent = getIntent();
        String title = intent.getStringExtra("boardTitle");
        String text = intent.getStringExtra("boardText");

        String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date()); // 등록 날짜입니다

        boardTitle.setText(title);
        boardText.setText(text);
        boardDate.setText(date);


        //댓글
        commentList = new ArrayList<>();
        commentRecyclerView = findViewById(R.id.board_textpage_comment_list);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList);
        commentRecyclerView.setAdapter(commentAdapter);

        boardTextpageComment = findViewById(R.id.board_textpage_comment);
        ImageButton commentSendB = findViewById(R.id.board_textpage_comment_sendB);



        commentSendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = boardTextpageComment.getText().toString();
                if (!commentText.isEmpty()) {
                    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                    commentList.add(new Comment("익명", commentText, currentDate));
                    commentAdapter.notifyDataSetChanged();
                    boardTextpageComment.setText("");
                }

            }
        });

    }
}
