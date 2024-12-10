package kr.ac.changwon.wa_ui_design;

public class HospitalReviewItem {
    private String reviewId;
    private String reviewText;
    private float reviewRating;
    private String reviewDate;

    public HospitalReviewItem(String reviewId, String reviewText, float reviewRating, String reviewDate) {
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.reviewRating = reviewRating;
        this.reviewDate = reviewDate;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getReviewRating() {
        return reviewRating;
    }

    public String getReviewDate() {
        return reviewDate;
    }
}