package ca.csf.mobile2.tp2.time;

public interface UtcTimeProvider {

    long getCurrentTimeInSeconds();

    void start();

    void stop();

    void addTimeListener(TimeListener timeListener);

    void removeTimeListener(TimeListener timeListener);

    interface TimeListener {
        void onTimeChanged(UtcTimeProvider eventSource);
    }

}