package com.radmagnet.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmsbn on 2/18/15.
 */
public class Category {

    private String name;

    private String color;

    private String value;

    private String icon;

    private boolean isSelected = false;

    private boolean isSection = false;

    public Category(String name, String color, String value, String icon, boolean isSelected) {
        this.name = name;
        this.color = color;
        this.value = value;
        this.icon = icon;
        this.isSelected = isSelected;
    }



    public Category(String name, String color, String icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
