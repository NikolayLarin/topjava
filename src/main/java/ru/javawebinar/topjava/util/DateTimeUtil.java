package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenInclusive(T value, T startValue, T endValue) {
        return value.compareTo(startValue) >= 0 && value.compareTo(endValue) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parse(String ld, DateTypeUtil type) {
        switch (type) {
            case START_DATE:
                return isEmpty(ld) ? LocalDate.MIN : ldParse(ld);
            case END_DATE:
                return isEmpty(ld) ? LocalDate.MAX : ldParse(ld);
        }
        return null;
    }

    public static LocalTime parse(String lt, TimeTypeUtil type) {
        switch (type) {
            case START_TIME:
                return isEmpty(lt) ? LocalTime.MIN : ltParse(lt);
            case END_TIME:
                return isEmpty(lt) ? LocalTime.MAX : ltParse(lt);
        }
        return null;
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

    public enum DateTypeUtil {
        START_DATE,
        END_DATE;
    }

    public enum TimeTypeUtil {
        START_TIME,
        END_TIME;
    }
}