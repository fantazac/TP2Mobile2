package ca.csf.mobile2.tp2.activity;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.csf.mobile2.tp2.R;
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
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    protected static final int MILLIS_DELAY = 1000;

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
        weatherColors = new int[]{R.color.sunnyBackground, R.color.cloudyBackground, R.color.rainBackground,R.color.snowBackground};

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
                               @ViewById(R.id.dateText) TextView dateTextView) {
        rootView = findViewById(R.id.rootView).getRootView();
        this.locationTextView = locationTextView;
        this.temperatureIconView = temperatureIconView;
        this.dateTextView = dateTextView;
    }

    @Background
    protected void getWeatherForecastBundle() {
        Call<WeatherForecastBundle> call = weatherForecastBundleRepository.retrieve();

        try {
            Response<WeatherForecastBundle> response = call.execute();
            if (response.isSuccessful()) {
                WeatherForecastBundle weatherForecastBundle = response.body();
                setLocation(weatherForecastBundle);
            } else {

                //Handle error
            }

        } catch (IOException e) {
            e.printStackTrace();
            //Handle error
        }
    }

    @UiThread
    protected void setLocation(WeatherForecastBundle weatherForecastBundle) {
        timedUtcTimeProvider = new TimedUtcTimeProvider(new Handler(), MILLIS_DELAY);
        timedUtcTimeProvider.start();
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
    }

}

