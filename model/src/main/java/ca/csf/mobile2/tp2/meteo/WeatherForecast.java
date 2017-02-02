package ca.csf.mobile2.tp2.meteo;

import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.time.Day;

public class WeatherForecast {

    private final Day day;
    private final WeatherType weather;
    private final MathFunction temperatureAccordingToUtcTimeFunction;

    public WeatherForecast(Day day,
                           WeatherType weather,
                           MathFunction temperatureAccordingToUtcTimeFunction) {
        this.day = day;
        this.weather = weather;
        this.temperatureAccordingToUtcTimeFunction = temperatureAccordingToUtcTimeFunction;
    }

    public Day getDay() {
        return day;
    }

    public WeatherType getWeather() {
        return weather;
    }

    public MathFunction getTemperatureAccordingToUtcTimeFunction() {
        return temperatureAccordingToUtcTimeFunction;
    }

    public boolean canGetTemperatureAt(long timeInSeconds) {
        return day.isOfTheSameDay(timeInSeconds);
    }

    public int getTemperatureAt(long timeInSeconds) {
        return (int) temperatureAccordingToUtcTimeFunction.getValue(timeInSeconds);
    }

}