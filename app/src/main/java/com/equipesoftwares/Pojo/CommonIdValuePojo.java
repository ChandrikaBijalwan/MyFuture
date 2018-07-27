package com.equipesoftwares.Pojo;

/**
 * Created by equipe on 7/19/2017.
 */

public class CommonIdValuePojo {
    private int key;

    private String value;
    public CommonIdValuePojo(int Key, String Value) {
        this.key = Key;
        this.value = Value;

    }

    public int getKey ()
    {
        return key;
    }

    public void setKey (int key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;

    }

}
