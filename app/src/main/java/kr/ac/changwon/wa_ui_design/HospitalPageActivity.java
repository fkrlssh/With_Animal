package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HospitalPageActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private MapView mapView;
    private HospitalReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_page);

        String userId = getIntent().getStringExtra("USER_ID");
        String hospitalName = getIntent().getStringExtra("HOSPITAL_NAME");
        float distance = getIntent().getFloatExtra("HOSPITAL_DISTANCE", 0.0f);

        TextView hospitalNameTextView = findViewById(R.id.hospital_page_name);
        TextView hospitalDistanceTextView = findViewById(R.id.hospital_page_distance);

        hospitalNameTextView.setText(hospitalName);
        hospitalDistanceTextView.setText(String.format("%.1f", distance / 1000) + " km");


        // 돌아가기 버튼 설정
        ImageButton hospitalReturnMain = findViewById(R.id.hospital_page_return_hospital);
        hospitalReturnMain.setOnClickListener(view -> {
            Intent intent = new Intent(HospitalPageActivity.this, HospitalActivity.class);
            startActivity(intent);
            finish();
        });


        Button reviewButton = findViewById(R.id.hospital_review_write);
        reviewButton.setOnClickListener(view -> {
            Intent intent = new Intent(HospitalPageActivity.this, HospitalReviewActivity.class);
            intent.putExtra("HOSPITAL_NAME", hospitalName); // 병원 이름 전달
            startActivity(intent);
        });


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            String reviewId = data.getStringExtra("REVIEW_ID");
            String reviewText = data.getStringExtra("REVIEW_TEXT");
            float reviewRating = data.getFloatExtra("REVIEW_RATING", 0);
            String reviewDate = data.getStringExtra("REVIEW_DATE");

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