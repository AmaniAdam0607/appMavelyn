package org.dromio.client001.utility;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.Duration;

public class CustomNotification {

    //TODO check the stacking behaviour and make it ux-ially pleasing, DO NOT USE internal variable to count or something like that, notification can be triggered by the user or programmatically and manual stacking using state can be an issue.

    private static final int NOTIFICATION_DURATION = (int) (Duration.ofSeconds(3).toMillis());

    public static void simpleErrorNotification(String message) {
        initAndShowNotification(message, CustomNotificationType.ERROR);
    }

    public static void simpleSuccessNotification(String message) {
        initAndShowNotification(message, CustomNotificationType.SUCCESS);
    }

    public static void simpleWarningNotification(String message) {
        initAndShowNotification(message, CustomNotificationType.WARNING);
    }

    public static void simpleInfoNotification(String message) {
        initAndShowNotification(message, CustomNotificationType.INFO);
    }

    private static void initAndShowNotification(String message, CustomNotificationType type) {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("background-color", type.getColor())
                .set("align-items", "center")
                .set("color", "white")
        ;
        layout.setPadding(true);
        layout.add(new Text(message), type.getIcon());

        Notification notification = new Notification(layout);
        notification.addClassNames("custom-notification");
        notification.setDuration(NOTIFICATION_DURATION);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

}
