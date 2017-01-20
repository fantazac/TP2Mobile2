package ca.csf.mobile2.tp2.meteo.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherType;
import ca.csf.mobile2.tp2.time.UtcDay;

public abstract class WeatherForecastJsonMixin extends WeatherForecast {

    @JsonCreator
    public WeatherForecastJsonMixin(@JsonProperty("utcDay") UtcDay utcDay, @JsonProperty("weather") WeatherType weather, @JsonProperty("temperatureAccordingToUtcTimeFunction") MathFunction temperatureAccordingToUtcTimeFunction) {
        super(utcDay, weather, temperatureAccordingToUtcTimeFunction);
    }

    @Override
    @JsonProperty("utcDay")
    public abstract UtcDay getUtcDay();

    @Override
    @JsonProperty("weather")
    public abstract WeatherType getWeather();

    @Override
    @JsonProperty("temperatureAccordingToUtcTimeFunction")
    public abstract MathFunction getTemperatureAccordingToUtcTimeFunction();

    @Override
    @JsonIgnore
    public abstract boolean canGetTemperatureAt(long utcTime);

    @Override
    @JsonIgnore
    public abstract int getTemperatureAt(long utcTime);

}
