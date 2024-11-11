package org.dromio.client001.utility;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.time.Duration;

public class CustomNotification {

    private static final int NOTIFICATION_DURATION = (int) (Duration.ofSeconds(5).toMillis());

    public static void simpleErrorNotification(String message) {
        initAndShowNotification(message, true);
    }

    public static void simpleSuccessNotification(String message) {
        initAndShowNotification(message, false);
    }

    private static void initAndShowNotification(String message, boolean isErrorNotification) {
        Notification notification = new Notification();
        if (isErrorNotification) {
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        else {
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        notification.setText(message);
        notification.setDuration(NOTIFICATION_DURATION);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }


}
