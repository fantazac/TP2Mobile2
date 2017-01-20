package ca.csf.mobile2.tp2.meteo;

import java.util.List;

public class WeatherForecastBundle {

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

    public List<WeatherForecast> getWeatherForecasts() {
        return weatherForecasts;
    }

    public int getTemperatureAt(long utcTime) {
        WeatherForecast weatherForecast = getWeatherForecastFor(utcTime);
        return weatherForecast == null ? 0 : weatherForecast.getTemperatureAt(utcTime);
    }

    public WeatherType getWeatherTypeAt(long utcTime) {
        WeatherForecast weatherForecast = getWeatherForecastFor(utcTime);
        return weatherForecast == null ? null : weatherForecast.getWeather();
    }

    private WeatherForecast getWeatherForecastFor(long utcTime) {
        for (WeatherForecast weatherForecast : weatherForecasts) {
            if (weatherForecast.canGetTemperatureAt(utcTime)) {
                return weatherForecast;
            }
        }
        return null;
    }

}