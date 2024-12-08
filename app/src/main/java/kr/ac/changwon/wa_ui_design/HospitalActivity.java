package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HospitalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital);

        ImageButton hospitalReturnMain = findViewById(R.id.hospital_return_home); // 병원정보 돌아가기 버튼
        hospitalReturnMain.setOnClickListener(new View.OnClickListener() { // 메인페이지로 돌아가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
