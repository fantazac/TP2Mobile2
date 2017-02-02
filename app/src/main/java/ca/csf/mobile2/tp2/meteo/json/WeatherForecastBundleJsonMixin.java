package ca.csf.mobile2.tp2.meteo.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Iterator;
import java.util.List;

import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;
import ca.csf.mobile2.tp2.meteo.WeatherType;

public abstract class WeatherForecastBundleJsonMixin extends WeatherForecastBundle {

    @JsonCreator
    public WeatherForecastBundleJsonMixin(@JsonProperty("locationName") String locationName,
                                          @JsonProperty("weatherForecasts") List<WeatherForecast> weatherForecasts) {
        super(locationName, weatherForecasts);
    }

    @Override
    @JsonProperty("locationName")
    public abstract String getLocationName();

    @Override
    @JsonIgnore
    public abstract WeatherForecast get(int index);

    @Override
    @JsonIgnore
    public abstract int getTemperatureAt(long timeInSeconds);

    @Override
    @JsonIgnore
    public abstract WeatherType getWeatherTypeAt(long timeInSeconds);

    @Override
    @JsonProperty("weatherForecasts")
    public abstract Iterator<WeatherForecast> iterator();

}
