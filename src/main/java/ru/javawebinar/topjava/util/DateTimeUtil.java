package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final LocalDateTime DUMMY = LocalDateTime.of(3000, 1, 1, 1, 1);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenInclusive(T value, T startValue, T endValue) {
        return value.compareTo(startValue) >= 0 && value.compareTo(endValue) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String ld) {
        return isEmpty(ld) ? DUMMY.toLocalDate() : ldParse(ld);

    }

    public static LocalTime parseTime(String lt) {
        return isEmpty(lt) ? DUMMY.toLocalTime() : ltParse(lt);

    }

    private static boolean isEmpty(String s) {
        return s.trim().isEmpty();
    }

    private static LocalDate ldParse(String ld) {
        return LocalDate.parse(ld, DATE_FORMATTER);
    }

    private static LocalTime ltParse(String lt) {
        return LocalTime.parse(lt, TIME_FORMATTER);
    }
}