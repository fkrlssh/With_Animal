package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class HospitalPageActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private MapView mapView;
    private HospitalReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_page);

        final String userId = getIntent().getStringExtra("USER_ID") != null ?
                getIntent().getStringExtra("USER_ID") : "익명"; // 일단 id 부분을 익명으로 표시

        String hospitalName = getIntent().getStringExtra("HOSPITAL_NAME");
        String hospitalAddress = getIntent().getStringExtra("HOSPITAL_ADDRESS");
        float distance = getIntent().getFloatExtra("HOSPITAL_DISTANCE", 0.0f);
        float rating = getIntent().getFloatExtra("HOSPITAL_RATING", 0.0f);
        String workingHours = getIntent().getStringExtra("HOSPITAL_WORKING_HOURS");
        String phoneNumber = getIntent().getStringExtra("HOSPITAL_PHONE_NUMBER");


        TextView hospitalNameTextView = findViewById(R.id.hospital_page_name);
        TextView hospitalAddressTextView = findViewById(R.id.hospital_page_place);
        TextView hospitalDistanceTextView = findViewById(R.id.hospital_page_distance);
        TextView hospitalRatingTextView = findViewById(R.id.hospital_page_rating);
        TextView hospitalTimeTextView = findViewById(R.id.hospital_time);
        TextView hospitalTelTextView = findViewById(R.id.hospital_tel);

        ListView reviewListView = findViewById(R.id.hospital_page_list);
        List<HospitalReviewItem> reviewList = new ArrayList<>();
        reviewAdapter = new HospitalReviewAdapter(this, reviewList);
        reviewListView.setAdapter(reviewAdapter);

        hospitalNameTextView.setText(hospitalName);
        hospitalAddressTextView.setText(hospitalAddress);
        hospitalDistanceTextView.setText(String.format("%.1f", distance / 1000) + " km");
        hospitalRatingTextView.setText(String.format("%.1f", rating));
        hospitalTimeTextView.setText(workingHours);
        hospitalTelTextView.setText(phoneNumber);


        ImageButton hospitalReturnMain = findViewById(R.id.hospital_page_return_hospital);
        hospitalReturnMain.setOnClickListener(view -> {
            Intent intent = new Intent(HospitalPageActivity.this, HospitalActivity.class);
            startActivity(intent);
            finish();
        });

        Button reviewButton = findViewById(R.id.hospital_review_write);
        reviewButton.setOnClickListener(view -> {
            Intent intent = new Intent(HospitalPageActivity.this, HospitalReviewActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivityForResult(intent, 100);
        });


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    private float calculateAverageRating(List<HospitalReviewItem> reviewList) { // 리뷰들 별점 평균
        if (reviewList == null || reviewList.isEmpty()) {
            return 0.0f; // 리뷰가 없으면 0점 반환
        }

        float sum = 0;
        for (HospitalReviewItem review : reviewList) {
            sum += review.getReviewRating();
        }
        return sum / reviewList.size();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            String reviewId = data.getStringExtra("REVIEW_ID");
            String reviewText = data.getStringExtra("REVIEW_TEXT");
            float reviewRating = data.getFloatExtra("REVIEW_RATING", 0);
            String reviewDate = data.getStringExtra("REVIEW_DATE");

            HospitalReviewItem newReview = new HospitalReviewItem(reviewId, reviewText, reviewRating, reviewDate);
            reviewAdapter.addReview(newReview);

            float averageRating = calculateAverageRating(reviewAdapter.getReviewList());
            TextView hospitalRatingTextView = findViewById(R.id.hospital_page_rating);
            hospitalRatingTextView.setText(String.format("%.1f", averageRating));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);

        LatLng hospitalLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(hospitalLocation).title("병원 위치"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLocation, 15));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



}