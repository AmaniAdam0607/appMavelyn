package org.dromio.client001.utility;

public enum AppColor {

    //message categories
    WARNING("rgb(230, 126, 34)"), ERROR("rgb(192, 57, 43)"), INFO("rgb(142, 68, 173)"), SUCCESS("rgb(39, 174, 96)"),

    //action buttons
    CONFIRM("rgb(22, 160, 133)"), CANCEL("rgb(44, 62, 80)");

    private final String color;

    private AppColor(String color) {
        this.color = color;
    }

    public String toString() {
        return color;
    }
}
