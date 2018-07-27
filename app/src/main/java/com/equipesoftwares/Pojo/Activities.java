package com.equipesoftwares.Pojo;

import com.equipesoftwares.common.Utils;

import java.util.List;

/**
 * Created by Monali on 7/12/2017.
 */

public class Activities {
    private String activity_id;

    private List<Images> activity_images;

    private String activities;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type = Utils.FLAG_ACTIVITIES;

    public String getActivity_id ()
    {
        return activity_id;
    }

    public void setActivity_id (String activity_id)
    {
        this.activity_id = activity_id;
    }



    public String getActivities ()
    {
        return activities;
    }

    public void setActivities (String activities)
    {
        this.activities = activities;
    }

    public List<Images> getActivity_images() {
        return activity_images;
    }

    public void setActivity_images(List<Images> activity_images) {
        this.activity_images = activity_images;
    }
}
