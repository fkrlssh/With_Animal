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

        ImageButton reviewReturnHospital = findViewById(R.id.review_return_hospital);
        reviewReturnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        String hospitalName = getIntent().getStringExtra("HOSPITAL_NAME");
        String userId = getIntent().getStringExtra("USER_ID"); // id
        TextView hospitalNameTextView = findViewById(R.id.hospital_review_name);
        hospitalNameTextView.setText(hospitalName);

        Button hospitalReviewAdd = findViewById(R.id.hospital_review_add);
        hospitalReviewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText reviewText = findViewById(R.id.hospital_review_write);
                RatingBar ratingBar = findViewById(R.id.hospital_Review_rating);

                String reviewContent = reviewText.getText().toString().trim();

                if (reviewContent.isEmpty()) {
                    reviewText.setError("내용을 입력하세요.");
                    reviewText.requestFocus();
                    return;
                }

                float rating = ratingBar.getRating();
                String reviewDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

                Intent intent = new Intent();
                intent.putExtra("REVIEW_ID", userId); // id
                intent.putExtra("HOSPITAL_NAME", hospitalName);
                intent.putExtra("REVIEW_TEXT", reviewContent);
                intent.putExtra("REVIEW_RATING", rating);
                intent.putExtra("REVIEW_DATE", reviewDate);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
