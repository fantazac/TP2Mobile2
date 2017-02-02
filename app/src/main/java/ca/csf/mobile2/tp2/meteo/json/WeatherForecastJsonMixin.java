package ca.csf.mobile2.tp2.meteo.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherType;
import ca.csf.mobile2.tp2.time.Day;

public abstract class WeatherForecastJsonMixin extends WeatherForecast {

    @JsonCreator
    public WeatherForecastJsonMixin(@JsonProperty("day") Day day,
                                    @JsonProperty("weather") WeatherType weather,
                                    @JsonProperty("temperatureAccordingToUtcTimeFunction") MathFunction temperatureAccordingToUtcTimeFunction) {
        super(day, weather, temperatureAccordingToUtcTimeFunction);
    }

    @Override
    @JsonProperty("day")
    public abstract Day getDay();

    @Override
    @JsonProperty("weather")
    public abstract WeatherType getWeather();

    @Override
    @JsonProperty("temperatureAccordingToUtcTimeFunction")
    public abstract MathFunction getTemperatureAccordingToUtcTimeFunction();

    @Override
    @JsonIgnore
    public abstract boolean canGetTemperatureAt(long timeInSeconds);

    @Override
    @JsonIgnore
    public abstract int getTemperatureAt(long timeInSeconds);

}
