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

        //BEN_REVIEW : L'enregistrement à WeatherListener devrait se faire ici. De toute façon, ce n'est jamais fait
        //             De plus, vous ne partez pas le "LiveWeather" avec la méthode "start".
    }

    @Bindable
    public int getCurrentTemperatureInCelsius() {
        return liveWeather.getCurrentTemperatureInCelsius();
    }

    //BEN_CORRECTION : La température n'est pas mise à jour en temps réel. Les raisons :
    //                      1 --> pas d'annotation @Bindable
    //                      2 --> pas d'appel à notifyPropertyChanged(BR.currentTemperatureInCelsius);
    public String getCurrentTemperatureString() {
        return String.valueOf(getCurrentTemperatureInCelsius()) + "°C";
    }

    @Bindable
    public WeatherType getCurrentWeatherType() {
        return liveWeather.getCurrentWeatherType();
    }

    //BEN_CORRECTION : Vous remarquerez que ce n'est jamais appellé.
    @Override
    public void onWeatherChanged(LiveWeather eventSource) {
        notifyPropertyChanged(BR.weather);
    }

    @BindingAdapter("weatherType")
    public static void bindWeatherTypeToDrawable(ImageView imageView, WeatherType weatherType) {
        //BEN_CORRECTION : NUll pointer expection ici que j'ai corrigé pour me permettre de voir le reste.
        if(weatherType != null) {
            switch (weatherType) {
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
    }

    @BindingAdapter("backgroundColor")
    public static void bindWeatherTypeToDrawable(View rootView, WeatherType weatherType) {
        //BEN_CORRECTION : NUll pointer expection ici que j'ai corrigé pour me permettre de voir le reste.
        if(weatherType != null) {
            switch (weatherType) {
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
}
