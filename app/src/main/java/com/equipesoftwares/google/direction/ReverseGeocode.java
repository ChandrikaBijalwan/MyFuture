package com.equipesoftwares.google.direction;
import java.util.ArrayList;

/*
 * @author Sunny
 * Used to convert Google dirextion json response to Object using GSON
 * */
public class ReverseGeocode {
    private ArrayList<Results> results;
    private String status;

    public ArrayList<Results> getResults(){
        return this.results;
    }
    public void setResults(ArrayList<Results> results){
        this.results = results;
    }
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
}
