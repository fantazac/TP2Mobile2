package ca.csf.mobile2.tp2.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;

import ca.csf.mobile2.tp2.BR;
import ca.csf.mobile2.tp2.meteo.LiveWeather;
import ca.csf.mobile2.tp2.meteo.WeatherType;

public class LiveWeatherViewModel extends BaseObservable implements LiveWeather.WeatherListener {

    private final LiveWeather liveWeather;

    public LiveWeatherViewModel(LiveWeather liveWeather) {
        this.liveWeather = liveWeather;
    }

    @Bindable
    public int getCurrentTemperatureInCelsius() {
        return liveWeather.getCurrentTemperatureInCelsius();
    }

    @Bindable
    public long getCurrentTime() {
        return liveWeather.getCurrentTime();
    }

    @Bindable
    public WeatherType getCurrentWeatherType() {
        return liveWeather.getCurrentWeatherType();
    }

    @Override
    public void onWeatherChanged(LiveWeather eventSource) {
        notifyPropertyChanged(BR.weather);
    }
}
