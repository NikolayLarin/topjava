package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DateTimeUtil() {
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static @Nullable LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static @Nullable LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static LocalDateTime createDateTime(@Nullable LocalDate date, LocalDate defaultDate, LocalTime time) {
//        does not work with PostgreSQL when Time has nanoseconds
//        PostgreSQL TIMESTAMP has "1 microsecond / 14 digits" Resolution
//        return LocalDateTime.of(date != null ? date : defaultDate, time);
        return LocalDateTime.of(date != null ? date : defaultDate, time).truncatedTo(ChronoUnit.MICROS);
    }
}