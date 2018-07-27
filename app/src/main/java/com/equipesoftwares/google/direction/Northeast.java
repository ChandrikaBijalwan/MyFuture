package com.equipesoftwares.google.direction;

/*
 * @author Sunny
 * Used to convert Google dirextion json response to Object using GSON
 * */

public class Northeast{
    private Number lat;
    private Number lng;

    public Number getLat(){
        return this.lat;
    }
    public void setLat(Number lat){
        this.lat = lat;
    }
    public Number getLng(){
        return this.lng;
    }
    public void setLng(Number lng){
        this.lng = lng;
    }
}