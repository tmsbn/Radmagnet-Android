package com.radmagnet.models;

import java.util.ArrayList;
import java.util.Date;


public class NewsResponse {

    private Date date;
    private ArrayList<News> data;

    public Date getDate() {
        return date;
    }

    public ArrayList<News> getData() {
        return data;
    }
}
