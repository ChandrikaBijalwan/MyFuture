package com.equipesoftwares.Pojo;

import com.equipesoftwares.common.Utils;

import java.util.List;

/**
 * Created by monali on 7/12/2017.
 */

public  class Achivements {
    private String achivement_id;

    private List<Images> achievement_images;


    private String achivements;

    public String getAchivement_id ()
    {
        return achivement_id;
    }

    public void setAchivement_id (String achivement_id)
    {
        this.achivement_id = achivement_id;
    }


    public String getAchivements ()
    {
        return achivements;
    }

    public void setAchivements (String achivements)
    {
        this.achivements = achivements;
    }

    public List<Images> getAchievement_images() {
        return achievement_images;
    }

    public void setAchievement_images(List<Images> achievement_images) {
        this.achievement_images = achievement_images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type = Utils.FLAG_ACHIEVEMENTS;
}
