package ru.demetriuzz.tools;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Форматирование Даты и Времени для объектов с JSON представлением.<br />
 * Часовой пояс игнорируется - всегда UTC!
 */
public class TimeFormatter {

    private static final ZoneOffset UTC = ZoneOffset.UTC;
    private static final DateTimeFormatter ISO_DT_WITHOUT_TZ = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter ISO_DATE_WITHOUT_TZ = DateTimeFormatter.ISO_DATE;

    private static final int DT_MAX_LENGTH = 19; // 2020-07-24T13:28:18
    private static final int DATE_MAX_LENGTH = 10; // 2020-07-24

    // parse

    public static long parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty() || timestamp.isBlank()) throw new RuntimeException("Empty timestamp");

        if (timestamp.length() < DT_MAX_LENGTH) throw new RuntimeException("timestamp is SHORT");

        // только то что нужно - всё что после, отбрасывается
        timestamp = timestamp.substring(0, DT_MAX_LENGTH);

        return Timestamp.from(LocalDateTime.parse(timestamp, ISO_DT_WITHOUT_TZ).toInstant(UTC)).getTime();
    }

    // при переводе даты в секунды берется начало дня как 00:00:00
    public static long parseDate(String date) {
        if (date == null || date.isEmpty() || date.isBlank()) throw new RuntimeException("Empty date");

        if (date.length() < DATE_MAX_LENGTH) throw new RuntimeException("date is SHORT");

        // только то что нужно - всё что после, отбрасывается
        date = date.substring(0, DATE_MAX_LENGTH);
        return Date.from(LocalDate.parse(date, ISO_DATE_WITHOUT_TZ).atStartOfDay(UTC).toInstant()).getTime();
    }

    // format

    public static String formatTimestamp(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp / 1_000L, 0, UTC).format(ISO_DT_WITHOUT_TZ);
    }

    public static String formatDate(long date) {
        return LocalDate.from(LocalDateTime.ofEpochSecond(date / 1_000L, 0, UTC))
                .atStartOfDay().format(ISO_DATE_WITHOUT_TZ);
    }

    // format from SQL

    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return null;
        return formatTimestamp(timestamp.getTime());
    }

    public static String formatDate(Date date) {
        if (date == null) return null;
        return formatDate(date.getTime());
    }

    // time zone for JDBC read and write

    public static TimeZone parseTimeZone(String zonedTimestamp) {
        if (zonedTimestamp == null || zonedTimestamp.isEmpty() || zonedTimestamp.isBlank())
            throw new RuntimeException("Empty zonedTimestamp");

        if (zonedTimestamp.length() < DT_MAX_LENGTH + 1) throw new RuntimeException("zonedTimestamp is SHORT");

        return TimeZone.getTimeZone(ZonedDateTime.parse(zonedTimestamp).getZone());
    }

}