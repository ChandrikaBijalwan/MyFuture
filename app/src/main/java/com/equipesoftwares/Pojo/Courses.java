package com.equipesoftwares.Pojo;

import java.util.List;

/**
 * Created by equipe on 7/12/2017.
 */

public  class Courses {

    private String course_name;
    private List<String> branches;

    public String getCourse_name ()
    {
        return "\n"+course_name+"\n";
    }

    public void setCourse_name (String course_name)
    {
        this.course_name = course_name;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }



}
