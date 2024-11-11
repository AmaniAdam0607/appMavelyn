package org.dromio.client001.utility;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CustomNotification {

    /**
     * Shows a notification with a custom message, type, and options.
     *
     * @param message The message to display in the notification.
     * @param type The type of notification: SUCCESS, ERROR, or WARNING.
     * @param timable If true, the notification will disappear after the specified duration.
     * @param duration The time in milliseconds the notification should be visible (if timable).
     * @param okAction Action to perform when OK is clicked (if applicable).
     */
    public static void showNotification(String message, NotificationType type, boolean timable, int duration, Runnable okAction) {
        Notification notification = createStyledNotification(message, type);

        // Define OK button and only show it when timable is false
        if (timable) {
            notification.setDuration(duration);
            notification.open();
        } else {
            Button okButton = new Button("OK", event -> {
                notification.close();
                if (okAction != null) okAction.run();
            });
            okButton.getStyle().set("margin-top", "10px");

            VerticalLayout contentLayout = new VerticalLayout(
                    new Span(message), okButton
            );
            contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            contentLayout.getStyle().set("padding", "10px");

            notification.add(contentLayout);
            notification.setDuration(-1); // Keeps it open until closed by user
            notification.open();
        }
    }

    /**
     * Creates a simple error notification.
     *
     * @param message The message to display in the notification.
     */
    public static void simpleErrorNotification(String message) {
        Notification notification = createStyledNotification(message, NotificationType.ERROR);
        notification.setDuration(3000); // Example duration for error
        notification.open();
    }

    /**
     * Creates a simple success notification.
     *
     * @param message The message to display in the notification.
     */
    public static void simpleSuccessNotification(String message) {
        Notification notification = createStyledNotification(message, NotificationType.SUCCESS);
        notification.setDuration(3000); // Example duration for success
        notification.open();
    }

    /**
     * Helper method to create a notification with styling based on its type.
     *
     * @param message The message for the notification.
     * @param type The type of notification.
     * @return A styled Notification instance.
     */
    private static Notification createStyledNotification(String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setPosition(Position.TOP_CENTER);
        notification.setDuration(3000);

        Span messageText = new Span(message);
        messageText.getStyle()
                .set("font-weight", "500")
                .set("font-size", "16px");

        // Apply color styles based on notification type
        switch (type) {
            case SUCCESS:
                messageText.getStyle().set("color", "#4CAF50"); // Soft green
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                break;
            case ERROR:
                messageText.getStyle().set("color", "#F44336"); // Soft red
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                break;
            case WARNING:
                messageText.getStyle().set("color", "#FF9800"); // Orange for warnings
                notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                break;
        }

        notification.add(messageText);
        return notification;
    }

    public enum NotificationType {
        SUCCESS, ERROR, WARNING
    }
}
