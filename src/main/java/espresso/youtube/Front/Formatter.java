package espresso.youtube.Front;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;


public class Formatter {
    public static String formatSeconds(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours < 1) {
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
    }
    public static String formatTime(Timestamp earlyTime){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime early = earlyTime.toLocalDateTime();
        Duration difference = Duration.between(early, now);

        long seconds = difference.getSeconds();
        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            long minutes = difference.toMinutes();
            return minutes + " minutes ago";
        } else if (seconds < 86400) {
            long hours = difference.toHours();
            return hours + " hours ago";
        } else if (seconds < 604800) {
            long days = difference.toDays();
            return days + " days ago";
        } else if (seconds < 2419200) {
            long weeks = difference.toDays() / 7;
            return weeks + " weeks ago";
        } else {
            long months = difference.toDays() / 30;
            return months + " months ago";
        }
    }

    public static String formatNumber(int number){
        if (number >= 1_000_000) {
            return (number / 1_000_000) + "m";
        } else if (number >= 1_000) {
            return (number / 1_000) + "k";
        } else {
            return String.valueOf(number);
        }
    }
}
