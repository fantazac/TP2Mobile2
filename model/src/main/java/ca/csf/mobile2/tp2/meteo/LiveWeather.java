package ca.csf.mobile2.tp2.meteo;

import ca.csf.mobile2.tp2.time.TimeProvider;

public class LiveWeather {

    private final WeatherForecastBundle weatherForecastBundle;
    private final TimeProvider timeProvider;
    private final TimeProvider.TimeListener timeListener;
    private WeatherListener weatherListener;

    public LiveWeather(WeatherForecastBundle weatherForecastBundle, TimeProvider timeProvider) {
        this.weatherForecastBundle = weatherForecastBundle;
        this.timeProvider = timeProvider;
        //TODO : Change to a Method Reference once Java 8 can be used
        this.timeListener = new UtcTimeProviderListener();
        this.weatherListener = null;
    }

    public int getCurrentTemperatureInCelsius() {
        return weatherForecastBundle.getTemperatureAt(timeProvider.getCurrentTimeInSeconds());
    }

    public long getCurrentTime() {
        return timeProvider.getCurrentTimeInSeconds();
    }

    public WeatherType getCurrentWeatherType() {
        return weatherForecastBundle.getWeatherTypeAt(timeProvider.getCurrentTimeInSeconds());
    }

    public void start(WeatherListener weatherListener) {
        if (this.weatherListener == null) {
            this.weatherListener = weatherListener;
            timeProvider.addTimeListener(timeListener);
            timeProvider.start();
        }
    }

    public void stop() {
        if (weatherListener != null) {
            timeProvider.stop();
            timeProvider.removeTimeListener(timeListener);
            weatherListener = null;
        }
    }

    private void onTimeChanged(TimeProvider eventSource) {
        weatherListener.onWeatherChanged(this);
    }

    public interface WeatherListener {
        void onWeatherChanged(LiveWeather eventSource);
    }

    //TODO : Delete this internal class once Java 8 can be used
    private class UtcTimeProviderListener implements TimeProvider.TimeListener {

        @Override
        public void onTimeChanged(TimeProvider eventSource) {
            LiveWeather.this.onTimeChanged(eventSource);
        }
    }

}
