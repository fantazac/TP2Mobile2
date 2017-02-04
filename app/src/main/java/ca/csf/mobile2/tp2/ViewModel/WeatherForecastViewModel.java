package ca.csf.mobile2.tp2.ViewModel;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ca.acodebreak.android.databind.list.DatabindableViewModel;
import ca.csf.mobile2.tp2.R;
import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherType;
import ca.csf.mobile2.tp2.time.Day;

//BEN_REVIEW : Autant il y a plein d'erreurs de DataBinding dans "MainActivty", autant ici c'est correct.
public class WeatherForecastViewModel extends DatabindableViewModel<WeatherForecast> {

    private static final int SECONDS_TO_MILLIS = 1000;

    private WeatherForecast weatherForecast;

    public WeatherForecastViewModel(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    @Bindable
    public Day getUtcDay() {
        return weatherForecast.getDay();
    }

    @Bindable
    public WeatherType getWeather() {
        return weatherForecast.getWeather();
    }

    @Bindable
    public int getMin() {
        return (int)weatherForecast.getTemperatureAccordingToUtcTimeFunction().getMinValue();
    }

    @Bindable
    public int getMax() {
        return (int)weatherForecast.getTemperatureAccordingToUtcTimeFunction().getMaxValue();
    }

    public Date getDate() {
        return new Date(getUtcDay().getTimeInSeconds() * SECONDS_TO_MILLIS);
    }

    public String getDayOfTheWeek(Date date) {
        date = new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(Calendar.getInstance().getTimeInMillis()));
        //BEN_REVIEW : Si c'est jaune, c'est un warning.
        //BEN_CORRECTION : De plus, constante...
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = simpleDateFormat.format(date);
        return dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);
    }

    @BindingAdapter("weatherType")
    public static void bindWeatherTypeToDrawable(TextView textView, WeatherType weatherType) {
        switch(weatherType) {
            case SUNNY:
                textView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_sunny, 0);
                break;
            case CLOUDY:
                textView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_cloudy, 0);
                break;
            case RAIN:
                textView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_rain, 0);
                break;
            case SNOW:
                textView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_snow, 0);
                break;
        }
    }

    @Override
    public WeatherForecast getModelObject() {
        return weatherForecast;
    }
}
