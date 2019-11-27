package ru.javawebinar.topjava.web.meal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalTime;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    @Override
    public LocalTime convert(String source) {
        return source == null || StringUtils.isEmpty(source) ? null : LocalTime.parse(source);
    }
}