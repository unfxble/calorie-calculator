package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, T startNoStrict, T endStrict) {
        return value.compareTo(startNoStrict) >= 0 && value.compareTo(endStrict) < 0;
    }

    public static <T extends Comparable<T>> boolean isBetween(T value, T startNoStrict, T endNoStrict) {
        return value.compareTo(startNoStrict) >= 0 && value.compareTo(endNoStrict) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate toLocalDate(String localDate) {
        return StringUtils.hasText(localDate) ? LocalDate.parse(localDate) : null;
    }

    public static LocalTime toLocalTime(String localTime) {
        return StringUtils.hasText(localTime) ? LocalTime.parse(localTime) : null;
    }
}

