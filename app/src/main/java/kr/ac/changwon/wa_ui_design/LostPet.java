package kr.ac.changwon.wa_ui_design;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;


public class LostPet {
    private String name;
    private String species;
    private String gender;
    private String description;
    private String lost_date;
    private String lost_location;
    private String photo_url;

    // Getter 및 Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLost_date() { return lost_date; }
    public void setLost_date(String lost_date) { this.lost_date = lost_date; }

    public String getLost_location() { return lost_location; }
    public void setLost_location(String lost_location) { this.lost_location = lost_location; }

    public String getPhoto_url() { return photo_url; }
    public void setPhoto_url(String photo_url) { this.photo_url = photo_url; }
}