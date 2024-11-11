package org.dromio.client001.utility;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTime {
    private LocalDateTime dateTime;

    public CustomDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // Format date as "dd/MM/yyyy"
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTime.format(formatter);
    }

    // Get time lapse as "1 min ago", "2 hours ago", etc.
    public static String getTimeLapse(LocalDateTime dateTimeGiven) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTimeGiven, now);

        if (duration.isNegative()) {
            return "in the future"; // For cases where dateTime is in the future
        }

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + (seconds == 1 ? " second ago" : " seconds ago");
        }

        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        }

        long hours = duration.toHours();
        if (hours < 24) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        }

        long days = duration.toDays();
        if (days < 7) {
            return days + (days == 1 ? " day ago" : " days ago");
        }

        long weeks = days / 7;
        if (weeks < 4) {
            return weeks + (weeks == 1 ? " week ago" : " weeks ago");
        }

        long months = weeks / 4;
        if (months < 12) {
            return months + (months == 1 ? " month ago" : " months ago");
        }

        long years = months / 12;
        return years + (years == 1 ? " year ago" : " years ago");
    }

    // For setting a new LocalDateTime if needed
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
}
