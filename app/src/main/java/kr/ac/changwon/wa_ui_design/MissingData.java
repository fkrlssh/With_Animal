package kr.ac.changwon.wa_ui_design;
import java.io.Serializable;

public class MissingData implements Serializable {
    private String name;
    private String species;
    private int age;
    private String gender;
    private String place;
    private String date;
    private String description;
    private String photoUri;

    public MissingData(String name, String species, int age, String gender, String place, String date, String description, String photoUri) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.gender = gender;
        this.place = place;
        this.date = date;
        this.description = description;
        this.photoUri = photoUri;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }
}
