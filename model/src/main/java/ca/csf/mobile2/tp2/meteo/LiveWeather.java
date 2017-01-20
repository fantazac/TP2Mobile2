package ca.csf.mobile2.tp2.meteo;

import ca.csf.mobile2.tp2.time.UtcTimeProvider;

public class LiveWeather {

    private final WeatherForecastBundle weatherForecastBundle;
    private final UtcTimeProvider utcTimeProvider;
    private final UtcTimeProvider.TimeListener timeListener;
    private WeatherListener weatherListener;

    public LiveWeather(WeatherForecastBundle weatherForecastBundle, UtcTimeProvider utcTimeProvider) {
        this.weatherForecastBundle = weatherForecastBundle;
        this.utcTimeProvider = utcTimeProvider;
        //TODO : Change to a Method Reference once Java 8 can be used
        this.timeListener = new UtcTimeProviderListener();
        this.weatherListener = null;
    }

    public int getCurrentTemperatureInCelsius() {
        return weatherForecastBundle.getTemperatureAt(utcTimeProvider.getCurrentTimeInSeconds());
    }

    public long getCurrentTime() {
        return utcTimeProvider.getCurrentTimeInSeconds();
    }

    public WeatherType getCurrentWeatherType() {
        return weatherForecastBundle.getWeatherTypeAt(utcTimeProvider.getCurrentTimeInSeconds());
    }

    public void start(WeatherListener weatherListener) {
        if (this.weatherListener == null) {
            this.weatherListener = weatherListener;
            utcTimeProvider.addTimeListener(timeListener);
            utcTimeProvider.start();
        }
    }

    public void stop() {
        if (weatherListener != null) {
            utcTimeProvider.stop();
            utcTimeProvider.removeTimeListener(timeListener);
            weatherListener = null;
        }
    }

    private void onTimeChanged(UtcTimeProvider eventSource) {
        weatherListener.onWeatherChanged(this);
    }

    public interface WeatherListener {
        void onWeatherChanged(LiveWeather eventSource);
    }

    //TODO : Delete this internal class once Java 8 can be used
    private class UtcTimeProviderListener implements UtcTimeProvider.TimeListener {

        @Override
        public void onTimeChanged(UtcTimeProvider eventSource) {
            LiveWeather.this.onTimeChanged(eventSource);
        }
    }

}
