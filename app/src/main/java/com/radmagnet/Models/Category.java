package com.radmagnet.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmsbn on 2/18/15.
 */
public class Category {

    private String name;

    private String color;

    private String value;

    private boolean isSelected = false;

    private boolean isSection = false;

    public Category(String name, String color, String value, boolean isSelected) {
        this.name = name;
        this.color = color;
        this.value = value;
        this.isSelected = isSelected;
    }

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setSection(boolean isSection) {
        this.isSection = isSection;
    }
}
