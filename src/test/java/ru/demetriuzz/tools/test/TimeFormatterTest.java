package ru.demetriuzz.tools.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.demetriuzz.tools.TimeFormatter;

import java.time.ZoneOffset;
import java.util.TimeZone;

public class TimeFormatterTest {

    @Test
    void error() {
        // ошибки входных значений

        // пустое значение даты и времени
        try {
            TimeFormatter.parseTimestamp(null);
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            TimeFormatter.parseTimestamp("");
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            TimeFormatter.parseTimestamp("    ");
            Assertions.fail();
        } catch (Exception ignore) {}

        // пустое значение даты
        try {
            TimeFormatter.parseDate(null);
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            TimeFormatter.parseDate("");
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            TimeFormatter.parseDate("    ");
            Assertions.fail();
        } catch (Exception ignore) {}

        // не полное значение даты и времени
        try {
            TimeFormatter.parseTimestamp("2019-01-01T10:11");
            Assertions.fail();
        } catch (Exception ignore) {}

        // не полное значение даты
        try {
            TimeFormatter.parseDate("2019-01");
            Assertions.fail();
        } catch (Exception ignore) {}

        // не полное значение даты и времени с часовым поясом
        try {
            TimeFormatter.parseTimeZone("2019-01-01T10:11:00");
            Assertions.fail();
        } catch (Exception ignore) {}
    }

    @Test
    void formatting() {
        // точность до одной секунды

        // начало эпохи
        final long epoch = 0L;
        final String dateEpoch = "1970-01-01";
        final String dateTimeEpoch = "1970-01-01T00:00:00";
        // туда
        Assertions.assertEquals(dateEpoch, TimeFormatter.formatDate(epoch));
        Assertions.assertEquals(dateTimeEpoch, TimeFormatter.formatTimestamp(epoch));
        // обратно
        Assertions.assertEquals(epoch, TimeFormatter.parseDate(dateEpoch));
        Assertions.assertEquals(epoch, TimeFormatter.parseTimestamp(dateTimeEpoch));

        // произвольная дата
        final long borderIn = 1_586_985_618_333L;
        final long borderOut = 1_586_985_618_000L;
        final String dateBorder = "2020-04-15";
        final String dateTimeBorder = "2020-04-15T21:20:18";
        // произвольная дата, для начала дня - назад к началу дня по секундам, минутам и часам
        final long borderForStartDay = 1_586_908_800_000L;
        // туда
        Assertions.assertEquals(dateTimeBorder, TimeFormatter.formatTimestamp(borderIn));
        Assertions.assertEquals(dateBorder, TimeFormatter.formatDate(borderIn));
        // обратно
        Assertions.assertEquals(borderOut, TimeFormatter.parseTimestamp(dateTimeBorder));
        Assertions.assertEquals(borderForStartDay, TimeFormatter.parseDate(dateBorder));

        // плюс часовой пояс на входе
        final String dateBorderWithTZ = "2020-04-15+0500";
        final String dateTimeBorderWithTZ = "2020-04-15T21:20:18+0500";
        Assertions.assertEquals(borderOut, TimeFormatter.parseTimestamp(dateTimeBorderWithTZ));
        Assertions.assertEquals(borderForStartDay, TimeFormatter.parseDate(dateBorderWithTZ));
    }

    @Test
    void zoneOffset() {
        // "-03:32" это -212 минут или -12720 секунд
        Assertions.assertEquals(TimeZone.getTimeZone(ZoneOffset.ofTotalSeconds(-12720)).getID(),
                TimeFormatter.parseTimeZone("2020-07-24T13:28:18-03:32").getID());

        // "+04:17" это +257 минут или 15420 секунд
        Assertions.assertEquals(TimeZone.getTimeZone(ZoneOffset.ofTotalSeconds(15420)).getID(),
                TimeFormatter.parseTimeZone("2020-07-24T13:28:18+04:17").getID());

        // Z - zulu time zone, +00:00
        Assertions.assertEquals(TimeZone.getTimeZone(ZoneOffset.ofTotalSeconds(0)).getID(),
                TimeFormatter.parseTimeZone("2020-07-24T13:28:18z").getID());
    }

}