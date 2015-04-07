package com.radmagnet.models;

import java.util.ArrayList;
import java.util.Date;


public class NewsResponse {

    private String date;
    private ArrayList<News> data;

    public String getDate() {
        return date;
    }

    public ArrayList<News> getData() {
        return data;
    }
}
