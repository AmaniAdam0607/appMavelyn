package org.dromio.client001.utility;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public enum CustomNotificationType {

    WARNING(AppColor.WARNING.toString(), new Icon(VaadinIcon.WARNING)),
    INFO(AppColor.INFO.toString(), new Icon(VaadinIcon.INFO)),
    SUCCESS(AppColor.SUCCESS.toString(), new Icon(VaadinIcon.CHECK)),
    ERROR(AppColor.ERROR.toString(), new Icon(VaadinIcon.EXCLAMATION_CIRCLE));

    private final Icon icon;
    private final String color;

    private CustomNotificationType(String color, Icon icon) {
        this.color = color;
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public Icon getIcon() {
        return icon;
    }
}
