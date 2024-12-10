package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HospitalReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_review);

        ImageButton reivewReturnHospital = findViewById(R.id.review_return_hospital);
        reivewReturnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalReviewActivity.this, HospitalPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String hospitalName = getIntent().getStringExtra("HOSPITAL_NAME");
        TextView hospitalNameTextView = findViewById(R.id.hospital_review_name);
        hospitalNameTextView.setText(hospitalName);

        Button submitButton = findViewById(R.id.hospital_review_add);
        submitButton.setOnClickListener(view -> {
            EditText reviewText = findViewById(R.id.hospital_review_write);
            RatingBar ratingBar = findViewById(R.id.hospital_Review_rating);

            String reviewContent = reviewText.getText().toString();
            float rating = ratingBar.getRating();
            String reviewDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            // 데이터 전달
            Intent intent = new Intent();
            intent.putExtra("HOSPITAL_NAME", hospitalName); // 병원 이름도 결과로 반환 가능
            intent.putExtra("REVIEW_TEXT", reviewContent);
            intent.putExtra("REVIEW_RATING", rating);
            intent.putExtra("REVIEW_DATE", reviewDate);

            setResult(RESULT_OK, intent);
            finish();
        });
    }

}
