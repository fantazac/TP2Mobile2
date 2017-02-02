package ca.csf.mobile2.tp2.time;

import android.os.Handler;

import java.util.Calendar;

public class FasterTimeProvider extends TimedTimeProvider {

    private final long timeToAdd;
    private long currentTime;

    public FasterTimeProvider(Handler timeHandler, long delayInMillisBetweenUpdates, long timeToAdd) {
        super(timeHandler, delayInMillisBetweenUpdates);

        this.timeToAdd = timeToAdd;
        this.currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
    }

    @Override
    public long getCurrentTimeInSeconds() {
        currentTime += timeToAdd;
        return currentTime;
    }

}
