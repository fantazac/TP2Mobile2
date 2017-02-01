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
import ca.csf.mobile2.tp2.time.UtcDay;

public class WeatherForecastViewModel extends DatabindableViewModel<WeatherForecast> {

    private static final int SECONDS_TO_MILLIS = 1000;

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

    public int[] getMinimumAndMaximumTemperaturesForDay(){
        int minimum = 100;
        int maximum = -100;

        int temperatureAtUtcTime;

        long utcTimeAtMidnight = weatherForecast.getUtcDay().getUtcDayTime();

        for(int i = 0; i < 24; i++){
            temperatureAtUtcTime = weatherForecast.getTemperatureAt(utcTimeAtMidnight + i * 3600);
            if(temperatureAtUtcTime > maximum){
                maximum = temperatureAtUtcTime;
            }
            if(temperatureAtUtcTime < minimum){
                minimum = temperatureAtUtcTime;
            }
        }

        return new int[]{minimum, maximum};

    }

    public Date getDate() {
        return new Date(getUtcDay().getUtcDayTime() * SECONDS_TO_MILLIS);
    }

    public String getDayOfTheWeek(Date date) {
        date = new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(Calendar.getInstance().getTimeInMillis()));
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
