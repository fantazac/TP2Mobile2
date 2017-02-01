package ca.csf.mobile2.tp2.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import ca.csf.mobile2.tp2.BR;
import ca.csf.mobile2.tp2.R;
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

    public String getCurrentTemperatureString() {
        return String.valueOf(getCurrentTemperatureInCelsius()) + "°C";
    }

    @Bindable
    public WeatherType getCurrentWeatherType() {
        return liveWeather.getCurrentWeatherType();
    }

    @Override
    public void onWeatherChanged(LiveWeather eventSource) {
        notifyPropertyChanged(BR.weather);
    }

    @BindingAdapter("weatherType")
    public static void bindWeatherTypeToDrawable(ImageView imageView, WeatherType weatherType) {
        switch(weatherType) {
            case SUNNY:
                imageView.setImageResource(R.drawable.ic_sunny);
                break;
            case CLOUDY:
                imageView.setImageResource(R.drawable.ic_cloudy);
                break;
            case RAIN:
                imageView.setImageResource(R.drawable.ic_rain);
                break;
            case SNOW:
                imageView.setImageResource(R.drawable.ic_snow);
                break;
        }
    }

    @BindingAdapter("backgroundColor")
    public static void bindWeatherTypeToDrawable(View rootView, WeatherType weatherType) {
        switch(weatherType) {
            case SUNNY:
                rootView.setBackgroundColor(rootView.getResources().getColor(R.color.sunnyBackground));
                break;
            case CLOUDY:
                rootView.setBackgroundColor(rootView.getResources().getColor(R.color.cloudyBackground));
                break;
            case RAIN:
                rootView.setBackgroundColor(rootView.getResources().getColor(R.color.rainBackground));
                break;
            case SNOW:
                rootView.setBackgroundColor(rootView.getResources().getColor(R.color.snowBackground));
                break;
        }
    }
}
