package ca.csf.mobile2.tp2.time;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day {

    private final String timeAsString;
    private final Calendar time;
    private final TimeZone timeZone;

    public Day(String timeAsString) {
        this.timeAsString = timeAsString;

        Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})\\s(GMT[+\\-]{1}\\d{1,2})");
        Matcher matcher = pattern.matcher(timeAsString);

        if (matcher.find() && matcher.groupCount() == 4) {
            int year = Integer.valueOf(matcher.group(1));
            int month = Integer.valueOf(matcher.group(2)) - 1; //First month (January) is 0, not 1
            int day = Integer.valueOf(matcher.group(3));
            String timeZoneId = matcher.group(4);

            time = Calendar.getInstance();
            timeZone = TimeZone.getTimeZone(timeZoneId);
            time.setTimeZone(timeZone);
            time.set(year, month, day, 0, 0, 0); //Removes hours, minutes and seconds, because they are not relevant
            time.set(Calendar.MILLISECOND, 0); //Remove milliseconds, because they are also not relevant
        } else {
            throw new IllegalArgumentException("Time string does not respect asked format : \"YYYY-MM-DD GMT+HH\" or \"YYYY-MM-DD GMT-HH\".");
        }
    }

    public String getTimeAsString() {
        return timeAsString;
    }

    public long getTimeInSeconds() {
        return time.getTimeInMillis() / 1000;
    }

    public boolean isOfTheSameDay(long timeInSeconds) {
        Calendar timeToCompare = Calendar.getInstance();
        timeToCompare.setTimeZone(timeZone);
        timeToCompare.setTimeInMillis(timeInSeconds * 1000);
        timeToCompare.set(Calendar.HOUR_OF_DAY, 0); //Calendar.HOUR_OF_DAY, because we use 24h format. Calendar.HOUR is for 12h format, with AM and PM.
        timeToCompare.set(Calendar.MINUTE, 0);
        timeToCompare.set(Calendar.SECOND, 0);
        timeToCompare.set(Calendar.MILLISECOND, 0);

        return time.equals(timeToCompare);
    }

}
