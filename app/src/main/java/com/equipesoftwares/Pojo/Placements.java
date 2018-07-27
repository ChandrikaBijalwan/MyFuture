package com.equipesoftwares.Pojo;

/**
 * Created by equipe on 7/12/2017.
 */

public class Placements {
    private String company_logo;

    private String company_name;

    private String placement_id;

    private String company_website;

    public String getCompany_logo ()
    {
        return company_logo;
    }

    public void setCompany_logo (String company_logo)
    {
        this.company_logo = company_logo;
    }

    public String getCompany_name ()
    {
        return "\t \t*\t"+company_name+"\n";
    }

    public void setCompany_name (String company_name)
    {
        this.company_name = company_name;
    }

    public String getPlacement_id ()
    {
        return placement_id;
    }

    public void setPlacement_id (String placement_id)
    {
        this.placement_id = placement_id;
    }

    public String getCompany_website ()
    {
        return company_website;
    }

    public void setCompany_website (String company_website)
    {
        this.company_website = company_website;
    }
}
