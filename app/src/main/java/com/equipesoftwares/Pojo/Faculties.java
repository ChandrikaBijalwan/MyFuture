package com.equipesoftwares.Pojo;

/**
 * Created by equipe on 7/12/2017.
 */

public class Faculties {

    private String is_principal;

    private String qualification;

    private String faculty_id;

    private String name;

    private String profile_pic;

    private String info;

    public String getIs_principal ()
    {
        return is_principal;
    }

    public void setIs_principal (String is_principal)
    {
        this.is_principal = is_principal;
    }

    public String getQualification ()
    {
        return "("+qualification+")\n";
    }

    public void setQualification (String qualification)
    {
        this.qualification = qualification;
    }

    public String getFaculty_id ()
    {
        return faculty_id;
    }

    public void setFaculty_id (String faculty_id)
    {
        this.faculty_id = faculty_id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getProfile_pic ()
    {
        return profile_pic;
    }

    public void setProfile_pic (String profile_pic)
    {
        this.profile_pic = profile_pic;
    }

    public String getInfo ()
    {
        return info;
    }

    public void setInfo (String info)
    {
        this.info = info;
    }
}
