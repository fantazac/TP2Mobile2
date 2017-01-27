package ca.csf.mobile2.tp2.activity;

import android.animation.TimeAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.csf.mobile2.tp2.BR;
import ca.csf.mobile2.tp2.R;
import ca.csf.mobile2.tp2.ViewModel.WeatherForecastBundleViewModel;
import ca.csf.mobile2.tp2.databinding.ActivityMainBinding;
import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.math.MathFunctionJsonMixin;
import ca.csf.mobile2.tp2.math.TrapezoidFunction;
import ca.csf.mobile2.tp2.math.TrapezoidFunctionJsonMixin;
import ca.csf.mobile2.tp2.meteo.LiveWeather;
import ca.csf.mobile2.tp2.meteo.WeatherForecast;
import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;
import ca.csf.mobile2.tp2.meteo.WeatherType;
import ca.csf.mobile2.tp2.meteo.json.WeatherForecastBundleJsonMixin;
import ca.csf.mobile2.tp2.meteo.json.WeatherForecastJsonMixin;
import ca.csf.mobile2.tp2.time.TimedUtcTimeProvider;
import ca.csf.mobile2.tp2.time.UtcDay;
import ca.csf.mobile2.tp2.time.UtcDayJsonMixin;
import ca.csf.mobile2.tp2.time.UtcTimeProvider;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    protected static final int MILLIS_DELAY = 1000;
    protected static final int SECONDS_TO_MILLIS = 1000;

    protected ObjectMapper objectMapper;

    protected LiveWeather liveWeather;
    protected TimedUtcTimeProvider timedUtcTimeProvider;

    private List<WeatherType> weatherTypes;
    private int[] weatherColors;
    private int[] weatherXMLs;

    protected TextView locationTextView;
    protected ImageView temperatureIconView;
    protected View rootView;
    protected TextView dateTextView;
    protected TextView currentTimeTextView;
    protected TextView temperatureTextView;

    private List<WeatherForecast> weatherForecasts;
    private WeatherForecastBundle weatherForecastBundle;

    private WeatherForecastBundleRepository weatherForecastBundleRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherTypes = new ArrayList<>();
        weatherTypes.add(WeatherType.SUNNY);
        weatherTypes.add(WeatherType.CLOUDY);
        weatherTypes.add(WeatherType.RAIN);
        weatherTypes.add(WeatherType.SNOW);

        weatherXMLs = new int[]{R.drawable.ic_sunny, R.drawable.ic_cloudy, R.drawable.ic_rain, R.drawable.ic_snow};
        weatherColors = new int[]{R.color.sunnyBackground, R.color.cloudyBackground, R.color.rainBackground, R.color.snowBackground};

        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(WeatherForecastBundle.class, WeatherForecastBundleJsonMixin.class);
        objectMapper.addMixIn(WeatherForecast.class, WeatherForecastJsonMixin.class);
        objectMapper.addMixIn(UtcDay.class, UtcDayJsonMixin.class);
        objectMapper.addMixIn(MathFunction.class, MathFunctionJsonMixin.class);
        objectMapper.addMixIn(TrapezoidFunction.class, TrapezoidFunctionJsonMixin.class);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(String.valueOf(getResources().getText(R.string.url))).addConverterFactory(JacksonConverterFactory.create(objectMapper)).build();
        weatherForecastBundleRepository = retrofit.create(WeatherForecastBundleRepository.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //isCounting = true;

        //countUpIndefinitely();
        getWeatherForecastBundle();
    }

    protected void injectViews(@ViewById(R.id.locationText) TextView locationTextView,
                               @ViewById(R.id.temperatureIcon) ImageView temperatureIconView,
                               @ViewById(R.id.dateText) TextView dateTextView,
                               @ViewById(R.id.currentTimeText) TextView currentTimeTextView,
                               @ViewById(R.id.temperatureText) TextView temperatureTextView) {
        rootView = findViewById(R.id.rootView);

        this.locationTextView = locationTextView;
        this.temperatureIconView = temperatureIconView;
        this.dateTextView = dateTextView;
        this.currentTimeTextView = currentTimeTextView;
        this.temperatureTextView = temperatureTextView;
    }

    @Background
    protected void getWeatherForecastBundle() {
        Call<WeatherForecastBundle> call = weatherForecastBundleRepository.retrieve();

        try {
            Response<WeatherForecastBundle> response = call.execute();
            if (response.isSuccessful()) {
                weatherForecastBundle = response.body();
                setInterface();
            } else {

                //Handle error
            }

        } catch (IOException e) {
            e.printStackTrace();
            //Handle error
        }
    }

    @UiThread
    protected void setInterface() {
        ActivityMainBinding binding = ActivityMainBinding.bind(rootView);
        binding.setForecastBundle(new WeatherForecastBundleViewModel(weatherForecastBundle));
        binding.setForecastLayoutId(R.layout.item_weather);
        binding.setForecastLayoutVariableId(BR.forecast);

        weatherForecasts = weatherForecastBundle.getWeatherForecasts();
        timedUtcTimeProvider = new TimedUtcTimeProvider(new Handler(), MILLIS_DELAY);
        timedUtcTimeProvider.start();
        timedUtcTimeProvider.addTimeListener(new UtcTimeProvider.TimeListener() {
            @Override
            public void onTimeChanged(UtcTimeProvider eventSource) {
                UpdateView();
            }
        });
        liveWeather = new LiveWeather(weatherForecastBundle, timedUtcTimeProvider);
        liveWeather.start(new LiveWeather.WeatherListener() {
            @Override
            public void onWeatherChanged(LiveWeather eventSource) {
                temperatureIconView.setImageResource(weatherXMLs[weatherTypes.indexOf(liveWeather.getCurrentWeatherType())]);
                rootView.setBackgroundColor(getResources().getColor((weatherColors[weatherTypes.indexOf(liveWeather.getCurrentWeatherType())])));
            }
        });
        locationTextView.setText(weatherForecastBundle.getLocationName());
        temperatureIconView.setImageResource(weatherXMLs[weatherTypes.indexOf(liveWeather.getCurrentWeatherType())]);
        rootView.setBackgroundColor(getResources().getColor((weatherColors[weatherTypes.indexOf(liveWeather.getCurrentWeatherType())])));
        UpdateView();
    }

    private void UpdateView(){
        for (WeatherForecast weatherForecast : weatherForecasts) {
            if (weatherForecast.canGetTemperatureAt(timedUtcTimeProvider.getCurrentTimeInSeconds())) {
                currentTimeTextView.setText(getCurrentTime(timedUtcTimeProvider));
                dateTextView.setText(getCurrentDay());
                temperatureTextView.setText(String.valueOf(liveWeather.getCurrentTemperatureInCelsius()) + "°C");
                break;
            }
        }
    }

    private String getCurrentTime(TimedUtcTimeProvider timedUtcTimeProvider) {

        Date date = new Date(timedUtcTimeProvider.getCurrentTimeInSeconds() * SECONDS_TO_MILLIS);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", dateTextView.getTextLocale());

        return simpleDateFormat.format(date);
    }

    private String getCurrentDay() {

        Date date = new Date(timedUtcTimeProvider.getCurrentTimeInSeconds() * SECONDS_TO_MILLIS);
        java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = simpleDateFormat.format(date);
        dayOfTheWeek = dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);

        String restOfDate = dateFormat.format(date);
        int indexOfFirstLetterOfMonth = restOfDate.indexOf(' ') + 1;
        restOfDate = restOfDate.substring(0, indexOfFirstLetterOfMonth) +
                restOfDate.substring(indexOfFirstLetterOfMonth, indexOfFirstLetterOfMonth+1).toUpperCase() +
                restOfDate.substring(indexOfFirstLetterOfMonth+1);

        return dayOfTheWeek + ", " + restOfDate;
    }

}

