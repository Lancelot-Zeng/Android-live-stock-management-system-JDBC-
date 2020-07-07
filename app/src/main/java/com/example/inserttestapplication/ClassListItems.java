package com.example.inserttestapplication;



public class ClassListItems
{

    public String url; //Image URL
    public String brick_type; //Name

    public ClassListItems(String brick_type,String url)//, String img)
    {
        this.url = url;
        this.brick_type = brick_type;
    }

    public String getImg() {
        return url;
    }

    public String getName() {
        return brick_type;
    }
}

