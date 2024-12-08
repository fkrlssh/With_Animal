package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MissingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing);

        ImageButton missingReturnHome = findViewById(R.id.missing_return_home); // 실종동물 돌아가기 버튼
        missingReturnHome.setOnClickListener(new View.OnClickListener() { // 메인 페이지로 돌아가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MissingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
