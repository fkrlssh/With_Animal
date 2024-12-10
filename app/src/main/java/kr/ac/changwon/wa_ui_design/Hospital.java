package kr.ac.changwon.wa_ui_design;

import android.location.Location;

public class Hospital {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private float distance;

    public Hospital(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = 0.0f;
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

    // 거리 계산 메서드
    public float calculateDistance(double userLatitude, double userLongitude) {
        float[] results = new float[1];
        Location.distanceBetween(userLatitude, userLongitude, this.latitude, this.longitude, results);
        return results[0]; // 결과는 미터 단위
    }
}
