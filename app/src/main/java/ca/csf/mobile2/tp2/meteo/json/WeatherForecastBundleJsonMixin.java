package ca.csf.mobile2.tp2.meteo.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;
import ca.csf.mobile2.tp2.meteo.WeatherType;

public abstract class WeatherForecastBundleJsonMixin extends WeatherForecastBundle {

    @JsonCreator
    public WeatherForecastBundleJsonMixin(@JsonProperty("locationName") String locationName, @JsonProperty("weatherForecasts") List<WeatherForecast> weatherForecasts) {
        super(locationName, weatherForecasts);
    }

    @Override
    @JsonProperty("locationName")
    public abstract String getLocationName();

    @Override
    @JsonProperty("weatherForecasts")
    public abstract List<WeatherForecast> getWeatherForecasts();

    @Override
    @JsonIgnore
    public abstract int getTemperatureAt(long utcTime);

    @Override
    @JsonIgnore
    public abstract WeatherType getWeatherTypeAt(long utcTime);

}
