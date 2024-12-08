package kr.ac.changwon.wa_ui_design;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id") // 서버에서 기대하는 필드 이름
    private String userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("phone")
    private String phone;

    // 회원가입 생성자
    public User(String userId, String userName, String email, String password, String phone) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    // 로그인 전용 생성자
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    // Getter와 Setter
}
