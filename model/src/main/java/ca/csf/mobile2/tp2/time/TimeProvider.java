package ca.csf.mobile2.tp2.time;

public interface TimeProvider {

    long getCurrentTimeInSeconds();

    void start();

    void stop();

    void addTimeListener(TimeListener timeListener);

    void removeTimeListener(TimeListener timeListener);

    interface TimeListener {
        void onTimeChanged(TimeProvider eventSource);
    }

}