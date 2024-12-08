package kr.ac.changwon.wa_ui_design;

public class foodListItem {
    private String name;
    private String dog;
    private String cat;
    private boolean dog_b;
    private boolean cat_b;

    public foodListItem(String name, String dog, String cat, boolean dog_b, boolean cat_b) {
        this.name = name;
        this.dog = dog;
        this.cat = cat;
        this.dog_b = dog_b;
        this.cat_b = cat_b;
    }

    public String getName(){
        return name;
    }

    public String getDog(){
        return dog;
    }

    public String getCat(){
        return cat;
    }

    public boolean isDog_b(){
        return dog_b;
    }

    public boolean isCat_b(){
        return cat_b;
    }

}
