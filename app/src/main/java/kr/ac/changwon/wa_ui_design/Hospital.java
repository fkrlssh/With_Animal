package kr.ac.changwon.wa_ui_design;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private float distance;
    private List<HospitalReviewItem> reviews; // 병원 리뷰 리스트
    private float averageRating; // 병원 평균 별점
    private String workingHours; // 진료 시간
    private String phoneNumber; // 전화번호

    public Hospital(String name, String address, double latitude, double longitude, String workingHours, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = 0.0f;
        this.reviews = new ArrayList<>(); // 초기화
        this.averageRating = 0.0f; // 초기값 설정
        this.workingHours = workingHours;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    // 리뷰 리스트 가져오기
    public List<HospitalReviewItem> getReviews() {
        return reviews;
    }

    // 평균 별점 가져오기
    public float getAverageRating() {
        return averageRating;
    }

    // 리뷰 추가 메서드
    public void addReview(HospitalReviewItem review) {
        reviews.add(review); // 리뷰 추가
        updateAverageRating(); // 평균 별점 업데이트
    }

    // 거리 계산 메서드
    public float calculateDistance(double userLatitude, double userLongitude) {
        float[] results = new float[1];
        Location.distanceBetween(userLatitude, userLongitude, this.latitude, this.longitude, results);
        return results[0]; // 결과는 미터 단위
    }

    // 평균 별점 업데이트
    private void updateAverageRating() {
        if (reviews.isEmpty()) {
            averageRating = 0.0f;
            return;
        }

        float sum = 0.0f;
        for (HospitalReviewItem review : reviews) {
            sum += review.getReviewRating();
        }
        this.averageRating = sum / reviews.size(); // 평균 계산
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
