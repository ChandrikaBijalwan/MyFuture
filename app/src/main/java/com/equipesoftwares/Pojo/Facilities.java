package com.equipesoftwares.Pojo;

import com.equipesoftwares.common.Utils;

import java.util.List;

/**
 * Created by equipe on 7/12/2017.
 */

public class Facilities {

    private List<Images> facility_images;
    private String facility_id;

    private String facilities;



    public String getFacility_id ()
    {
        return facility_id;
    }

    public void setFacility_id (String facility_id)
    {
        this.facility_id = facility_id;
    }

    public String getFacilities ()
    {
        return facilities;
    }

    public void setFacilities (String facilities)
    {
        this.facilities = facilities;
    }

    public List<Images> getFacility_images() {
        return facility_images;
    }

    public void setFacility_images(List<Images> facility_images) {
        this.facility_images = facility_images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type = Utils.FLAG_FACILITIES;
}
