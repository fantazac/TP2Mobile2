package ca.csf.mobile2.tp2.time;

import android.os.Handler;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class TimedTimeProvider implements TimeProvider {

    private final Handler timeHandler;
    private final long delayInMillisBetweenUpdates;
    private final List<TimeListener> timeListeners;
    /*
     * Keep the runnable to be able to cancel it and to prevent re-creating it each time. We do this because Java 8
     * method references doesn't give the same object, but a new anonymous class every time.
     */
    private final Runnable timeHandlerRunnable;

    private boolean isRunning;

    public TimedTimeProvider(Handler timeHandler, long delayInMillisBetweenUpdates) {
        this.timeHandler = timeHandler;
        this.delayInMillisBetweenUpdates = delayInMillisBetweenUpdates;
        timeListeners = new LinkedList<>();
        //timeHandlerRunnable = this::onTimeChanged; //TODO : Uncomment this when DataBinding will be supported by Jack and Java 8 is enabled
        timeHandlerRunnable = new TimeHandlerListener(); //TODO : Remove this when DataBinding will be supported by Jack and Java 8 is enabled
        isRunning = false;
    }

    @Override
    public long getCurrentTimeInSeconds() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;
            scheduleNextTimerTick();
        }
    }

    @Override
    public void stop() {
        if (isRunning) {
            cancelNextTimerTick();
            isRunning = false;
        }
    }

    @Override
    public void addTimeListener(TimeListener timeListener) {
        timeListeners.add(timeListener);
    }

    @Override
    public void removeTimeListener(TimeListener timeListener) {
        timeListeners.remove(timeListener);
    }

    private void scheduleNextTimerTick() {
        timeHandler.postDelayed(timeHandlerRunnable, delayInMillisBetweenUpdates);
    }

    private void cancelNextTimerTick() {
        timeHandler.removeCallbacks(timeHandlerRunnable);
    }

    private void onTimeChanged() {
        if (isRunning) {
            for (TimeListener timeListener : timeListeners) {
                timeListener.onTimeChanged(this);
            }
            scheduleNextTimerTick();
        }
    }

    //TODO : Remove this when DataBinding will be supported by Jack and Java 8 is enabled
    private class TimeHandlerListener implements Runnable {

        @Override
        public void run() {
            TimedTimeProvider.this.onTimeChanged();
        }
    }
    //END Remove this when DataBinding will be supported by Jack and Java 8 is enabled

}
