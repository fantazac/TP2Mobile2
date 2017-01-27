package ca.csf.mobile2.tp2.ViewModel;

import android.databinding.BaseObservable;

import ca.acodebreak.android.databind.list.DatabindableViewModel;
import ca.acodebreak.android.databind.list.DatabindableViewModelList;
import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;

public class WeatherForecastBundleViewModel extends DatabindableViewModelList<WeatherForecast> {

    private final WeatherForecastBundle weatherForecastBundle;

    public WeatherForecastBundleViewModel(WeatherForecastBundle weatherForecastBundle) {
        this.weatherForecastBundle = weatherForecastBundle;

        for(WeatherForecast forecast : this.weatherForecastBundle.getWeatherForecasts()) {
            addModelItem(forecast);
        }
    }

    @Override
    protected DatabindableViewModel<WeatherForecast> createViewModel(WeatherForecast weatherForecast) {
        return new WeatherForecastViewModel(weatherForecast);
    }
}
