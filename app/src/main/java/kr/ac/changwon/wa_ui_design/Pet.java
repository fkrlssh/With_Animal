package kr.ac.changwon.wa_ui_design;

import com.google.gson.annotations.SerializedName;

public class Pet {
    @SerializedName("user_id") // 서버와의 통신에서 "user_id"로 매핑
    private String userId;

    @SerializedName("pet_name")
    private String name;

    @SerializedName("species")
    private String species;

    @SerializedName("age")
    private String age;

    @SerializedName("gender")
    private String gender;

    @SerializedName("photo_url")
    private String photo;

    // userId를 포함한 생성자
    public Pet(String userId, String name, String species, String age, String gender, String photo) {
        this.userId = userId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.gender = gender;
        this.photo = photo;
    }

    // userId 없이 사용할 수 있는 기본 생성자 (photo를 빈 문자열로 설정)
    public Pet(String name, String species, String age, String gender) {
        this("", name, species, age, gender, "");
    }

    // Getter 및 Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
