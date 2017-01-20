package ca.csf.mobile2.tp2.meteo;

import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.time.UtcDay;

public class WeatherForecast {

    private final UtcDay utcDay;
    private final WeatherType weather;
    private final MathFunction temperatureAccordingToUtcTimeFunction;

    public WeatherForecast(UtcDay utcDay,
                           WeatherType weather,
                           MathFunction temperatureAccordingToUtcTimeFunction) {
        this.utcDay = utcDay;
        this.weather = weather;
        this.temperatureAccordingToUtcTimeFunction = temperatureAccordingToUtcTimeFunction;
    }

    public UtcDay getUtcDay() {
        return utcDay;
    }

    public WeatherType getWeather() {
        return weather;
    }

    public MathFunction getTemperatureAccordingToUtcTimeFunction() {
        return temperatureAccordingToUtcTimeFunction;
    }

    public boolean canGetTemperatureAt(long utcTime) {
        return utcDay.isOfTheSameDay(utcTime);
    }

    public int getTemperatureAt(long utcTime) {
        return (int) temperatureAccordingToUtcTimeFunction.getValue(utcTime);
    }

}
