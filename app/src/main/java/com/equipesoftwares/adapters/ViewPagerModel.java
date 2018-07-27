package com.equipesoftwares.adapters;

/**
 * Created by My_Machine on 18/09/2016.
 */
public class ViewPagerModel {
    private String description,title;
    private int image;
    private  String images;

     //Constructor #1
    public ViewPagerModel(String description, String title) {
        super();
        this.description = description;
        this.title = title;
    }

    public ViewPagerModel(String description, String title, int image) {
        super();
        this.description = description;
        this.title = title;
        this.image=image;
    }

    // Constructor #2
    public ViewPagerModel(String image) {
        super();
        this.images = image;
    }

    // Constructor #3
    public ViewPagerModel(String description, int image) {
        super();
        this.description = description;
        this.image = image;
    }

    // Getter-Setters
    public String getDescription() {
        return description;
    }
    public String getTitle() {return title;}

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

