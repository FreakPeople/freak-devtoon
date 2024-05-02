package yjh.devtoon.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
        return LocalDateTime.now().format(formatter);
    }

}
