package kr.ac.changwon.wa_ui_design;

public class Pet {
    private String name;
    private String species;
    private String age;
    private String gender;

    public Pet(String name, String species, String age, String gender) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}