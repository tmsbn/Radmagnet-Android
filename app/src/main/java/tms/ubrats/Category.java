package tms.ubrats;

/**
 * Created by tmsbn on 2/18/15.
 */
public class Category {

    String name;
    String color;
    String value;
    boolean isSelected = false;
    boolean isSection = false;

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
}
