package com.equipesoftwares.google.direction;

import java.util.ArrayList;

/*
 * @author Sunny
 * Used to convert Google dirextion json response to Object using GSON
 * */
public class Results {
    private ArrayList<Address_components> address_components;
    private String formatted_address;
    private Geometry geometry;
    private ArrayList<String> types;

    public ArrayList<Address_components> getAddress_components(){
        return this.address_components;
    }
    public void setAddress_components(ArrayList<Address_components> address_components){
        this.address_components = address_components;
    }
    public String getFormatted_address(){
        return this.formatted_address;
    }
    public void setFormatted_address(String formatted_address){
        this.formatted_address = formatted_address;
    }
    public Geometry getGeometry(){
        return this.geometry;
    }
    public void setGeometry(Geometry geometry){
        this.geometry = geometry;
    }
    public ArrayList<String> getTypes(){
        return this.types;
    }
    public void setTypes(ArrayList<String> types){
        this.types = types;
    }

}
