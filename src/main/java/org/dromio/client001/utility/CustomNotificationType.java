package org.dromio.client001.utility;


public enum CustomNotificationType {

    WARNING(AppColor.WARNING.toString()),
    INFO(AppColor.INFO.toString()),
    SUCCESS(AppColor.SUCCESS.toString()),
    ERROR(AppColor.ERROR.toString());

    private final String color;

    private CustomNotificationType(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
