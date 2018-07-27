package com.equipesoftwares.Pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by equipe on 7/12/2017.
 */

public class SchoolDetailPojo {
//private String about;

    private String city;


    private List<Placements> placements;

    private String classes;

    private List<Faculties> faculties;

    private Boolean is_school;

    public Boolean getIs_school() {
        return is_school;
    }

    public void setIs_school(Boolean is_school) {
        this.is_school = is_school;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String logo;

    private List<Courses> courses;

    private String website;

    private String about;

    private int board_id;
    private String management_id;


    private String id;

    private String medium;

    private String email;

    private String address;

    private String name;

    private String phone_no;

    private String created_at;

    private List<Images> images;

    private String longitude;

    private ArrayList<Activities> activities;

    private String latitude;



    private ArrayList<Facilities> facilities;



    public List<Faculties> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculties> faculties) {
        this.faculties = faculties;
    }

    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public ArrayList<Activities> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activities> activities) {
        this.activities = activities;
    }

    public ArrayList<Facilities> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<Facilities> facilities) {
        this.facilities = facilities;
    }

    public ArrayList<Achivements> getAchivements() {
        return achivements;
    }

    public void setAchivements(ArrayList<Achivements> achivements) {
        this.achivements = achivements;
    }

    private ArrayList<Achivements> achivements;


    public String getClasses ()
    {
        return classes;
    }

    public void setClasses (String classes)
    {
        this.classes = classes;
    }

    public String getLogo ()
    {
        return logo;
    }

    public void setLogo (String logo)
    {
        this.logo = logo;
    }


    public String getWebsite ()
    {
        return website;
    }

    public void setWebsite (String website)
    {
        this.website = website;
    }

    public String getAbout ()
    {
        return about;
    }

    public void setAbout (String about)
    {
        this.about = about;
    }

    public int getBoard_id ()
    {
        return board_id;
    }

    public void setBoard_id (int board_id)
    {
        this.board_id = board_id;
    }
    public String getManagement_id ()
    {
        return management_id;
    }

    public void setManagement_id (String management_id)
    {
        this.management_id = management_id;
    }


    public String getMedium ()
    {
        return medium;
    }

    public void setMedium (String medium)
    {
        this.medium = medium;
    }


    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getPhone_no ()
    {
        return phone_no;
    }

    public void setPhone_no (String phone_no)
    {
        this.phone_no = phone_no;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }



    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }



    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }


    public List<Placements> getPlacements() {
        return placements;
    }

    public void setPlacements(List<Placements> placements) {
        this.placements = placements;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
