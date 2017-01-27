package ca.csf.mobile2.tp2.ViewModel;

import android.databinding.Bindable;

import ca.acodebreak.android.databind.list.DatabindableViewModel;
import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherType;
import ca.csf.mobile2.tp2.time.UtcDay;

public class WeatherForecastViewModel extends DatabindableViewModel<WeatherForecast> {

    private WeatherForecast weatherForecast;

    public WeatherForecastViewModel(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    @Bindable
    public UtcDay getUtcDay() {
        return weatherForecast.getUtcDay();
    }

    @Bindable
    public WeatherType getWeather() {
        return weatherForecast.getWeather();
    }

    @Bindable
    public MathFunction getTemperatureAccordingToUtcTimeFunction() {
        return weatherForecast.getTemperatureAccordingToUtcTimeFunction();
    }

    @Override
    public WeatherForecast getModelObject() {
        return weatherForecast;
    }
}
