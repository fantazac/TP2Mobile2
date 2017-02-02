package ca.csf.mobile2.tp2.ViewModel;

import ca.acodebreak.android.databind.list.DatabindableViewModel;
import ca.acodebreak.android.databind.list.DatabindableViewModelList;
import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;

public class WeatherForecastBundleViewModel extends DatabindableViewModelList<WeatherForecast> {

    private final WeatherForecastBundle weatherForecastBundle;

    public WeatherForecastBundleViewModel(WeatherForecastBundle weatherForecastBundle) {
        this.weatherForecastBundle = weatherForecastBundle;

        for (WeatherForecast weatherForecast : weatherForecastBundle) {
            addModelItem(weatherForecast);
        }
    }

    @Override
    protected DatabindableViewModel<WeatherForecast> createViewModel(WeatherForecast weatherForecast) {
        return new WeatherForecastViewModel(weatherForecast);
    }
}
