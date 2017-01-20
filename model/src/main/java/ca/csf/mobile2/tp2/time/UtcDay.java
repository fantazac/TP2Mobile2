package ca.csf.mobile2.tp2.time;


public class UtcDay {

    private final long utcDayTime;

    public UtcDay(long utcTime) {
        this.utcDayTime = getUtcDayTime(utcTime);
    }

    public long getUtcDayTime() {
        return utcDayTime;
    }

    public boolean isOfTheSameDay(long utcTime) {
        return utcDayTime == getUtcDayTime(utcTime);
    }

    private long getUtcDayTime(long utcTime) {
        utcTime /= 60; //To minutes, truncate remaining seconds
        utcTime /= 60; //To hours, truncate remaining minutes
        utcTime /= 24; //To days, truncate remaining hours
        return utcTime * 24 * 60 * 60; //To seconds
    }
}
