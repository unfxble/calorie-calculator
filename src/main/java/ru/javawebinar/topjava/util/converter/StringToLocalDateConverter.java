package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public LocalDate convert(String source) {
        return StringUtils.hasLength(source) ? LocalDate.parse(source, DateTimeFormatter.ofPattern(DATE_PATTERN)) : null;
    }
}
