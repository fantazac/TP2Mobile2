package ca.csf.mobile2.tp2.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DayTest {

    private static final int GMT_MIN = -12;
    private static final int GMT_MAX = 12;

    @Test
    public void canGetTimeAsString() {
        for (int i = GMT_MIN; i <= GMT_MAX; i++) {
            String timeAsString = "2017-02-01 GMT" + valueOfWithSign(i);

            Day day = new Day(timeAsString);

            assertEquals(timeAsString, day.getTimeAsString());
        }
    }

    @Test
    public void canTellIfIsOfTheSameDay() {
        for (int i = GMT_MIN; i <= GMT_MAX; i++) {
            Day day = new Day("2017-02-01 GMT" + valueOfWithSign(i));

            assertFalse(day.isOfTheSameDay(getTime("2017-01-31 00:00:00 GMT" + valueOfWithSign(i))));
            assertFalse(day.isOfTheSameDay(getTime("2017-01-31 01:00:00 GMT" + valueOfWithSign(i))));
            assertFalse(day.isOfTheSameDay(getTime("2017-01-31 12:00:00 GMT" + valueOfWithSign(i))));
            assertFalse(day.isOfTheSameDay(getTime("2017-01-31 23:59:59 GMT" + valueOfWithSign(i))));

            assertTrue(day.isOfTheSameDay(getTime("2017-02-01 00:00:00 GMT" + valueOfWithSign(i))));
            assertTrue(day.isOfTheSameDay(getTime("2017-02-01 01:00:00 GMT" + valueOfWithSign(i))));
            assertTrue(day.isOfTheSameDay(getTime("2017-02-01 12:00:00 GMT" + valueOfWithSign(i))));
            assertTrue(day.isOfTheSameDay(getTime("2017-02-01 23:59:59 GMT" + valueOfWithSign(i))));

            assertFalse(day.isOfTheSameDay(getTime("2017-02-02 00:00:00 GMT" + valueOfWithSign(i))));
            assertFalse(day.isOfTheSameDay(getTime("2017-02-02 01:00:00 GMT" + valueOfWithSign(i))));
            assertFalse(day.isOfTheSameDay(getTime("2017-02-02 12:00:00 GMT" + valueOfWithSign(i))));
            assertFalse(day.isOfTheSameDay(getTime("2017-02-02 23:59:59 GMT" + valueOfWithSign(i))));
        }
    }

    @Test
    public void canGetTimeAsSecondsWithoutHoursMinutesAndSecondsOfDay() {
        for (int i = GMT_MIN; i <= GMT_MAX; i++) {
            Day day = new Day("2017-02-01 GMT" + valueOfWithSign(i));

            assertEquals(getTime("2017-02-01 00:00:00 GMT" + valueOfWithSign(i)), day.getTimeInSeconds());
        }
    }

    private String valueOfWithSign(int value) {
        return (value >= 0 ? "+" : "") + String.valueOf(value);
    }

    private long getTime(String timeAsString) {
        Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})\\s(GMT[+\\-]{1}\\d{1,2})");

        Matcher matcher = pattern.matcher(timeAsString);

        if (matcher.find() && matcher.groupCount() == 7) {
            int year = Integer.valueOf(matcher.group(1));
            int month = Integer.valueOf(matcher.group(2)) - 1; //First month (January) is 0, not 1
            int day = Integer.valueOf(matcher.group(3));
            int hour = Integer.valueOf(matcher.group(4));
            int minutes = Integer.valueOf(matcher.group(5));
            int seconds = Integer.valueOf(matcher.group(6));
            String timeZoneId = matcher.group(7);

            Calendar calendar = Calendar.getInstance();
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
            calendar.setTimeZone(timeZone);
            calendar.set(year, month, day, hour, minutes, seconds);

            return calendar.getTimeInMillis() / 1000;
        }

        throw new IllegalArgumentException("Time as string is not valid");
    }

}