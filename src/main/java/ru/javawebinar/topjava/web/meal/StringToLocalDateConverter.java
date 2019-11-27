package ru.javawebinar.topjava.web.meal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        return source == null || StringUtils.isEmpty(source) ? null : LocalDate.parse(source);
    }
}