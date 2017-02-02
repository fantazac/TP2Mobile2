package ca.csf.mobile2.tp2.meteo;

import java.util.Iterator;
import java.util.List;

public class WeatherForecastBundle implements Iterable<WeatherForecast> {

    private final String locationName;
    private final List<WeatherForecast> weatherForecasts;

    public WeatherForecastBundle(String locationName, List<WeatherForecast> weatherForecasts) {
        this.locationName = locationName;
        this.weatherForecasts = weatherForecasts;

        if (weatherForecasts.size() == 0) {
            throw new IllegalArgumentException("WeatherForecastBundle need at least one WeatherForecast.");
        }
    }

    public String getLocationName() {
        return locationName;
    }

    public WeatherForecast get(int index) {
        return weatherForecasts.get(index);
    }

    public int getTemperatureAt(long timeInSeconds) {
        WeatherForecast weatherForecast = getWeatherForecastFor(timeInSeconds);
        return weatherForecast == null ? 0 : weatherForecast.getTemperatureAt(timeInSeconds);
    }

    public WeatherType getWeatherTypeAt(long timeInSeconds) {
        WeatherForecast weatherForecast = getWeatherForecastFor(timeInSeconds);
        return weatherForecast == null ? null : weatherForecast.getWeather();
    }

    private WeatherForecast getWeatherForecastFor(long timeInSeconds) {
        for (WeatherForecast weatherForecast : weatherForecasts) {
            if (weatherForecast.canGetTemperatureAt(timeInSeconds)) {
                return weatherForecast;
            }
        }
        return null;
    }

    @Override
    public Iterator<WeatherForecast> iterator() {
        return weatherForecasts.iterator();
    }

}