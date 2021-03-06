package ca.csf.mobile2.tp2.activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ca.csf.mobile2.tp2.BR;
import ca.csf.mobile2.tp2.R;
import ca.csf.mobile2.tp2.ViewModel.LiveWeatherViewModel;
import ca.csf.mobile2.tp2.ViewModel.WeatherForecastBundleViewModel;
import ca.csf.mobile2.tp2.ViewModel.WeatherForecastViewModel;
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
import ca.csf.mobile2.tp2.time.Day;
import ca.csf.mobile2.tp2.time.DayJsonMixin;
import ca.csf.mobile2.tp2.time.FasterTimeProvider;
import ca.csf.mobile2.tp2.time.TimeProvider;
import ca.csf.mobile2.tp2.time.TimedUtcTimeProvider;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final int SECONDS_TO_MILLIS = 1000;
    protected static final int MILLIS_DELAY = 1000;

    //BEN_CORRECTION : Pourquoi ce n'est pas juste une variable locale à la méthode "onCreate". Est-ce vraiment nécessaire que ce soit un attribut de classe ?
    protected ObjectMapper objectMapper;

    // BEN_REVIEW : De préférence, lorsque vous le pouvez, comme ici, utilisez le type de base (l'abstraction) et non pas le type concret.
    //              Je l'ai modifiné pour vous montrer. Cela permet une plus grande souplesse.
    protected TimeProvider timedUtcTimeProvider;

    //BEN_CORRECTION: Inutile. Créé, mais jamais utilisé.
    private List<WeatherType> weatherTypes;

    protected View rootView;
    //BEN_CORRECTION : Les 3 prochains ne devrait même pas être là vu que vous faites usage du DataBinding.
    protected TextView locationTextView;
    protected TextView dateTextView;
    protected TextView currentTimeTextView;

    //BEN_REVIWE : Inutile vu que vous avez déjà weatherForecastBundle.
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

        weatherForecasts = new LinkedList<>();

        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(WeatherForecastBundle.class, WeatherForecastBundleJsonMixin.class);
        objectMapper.addMixIn(WeatherForecast.class, WeatherForecastJsonMixin.class);
        objectMapper.addMixIn(Day.class, DayJsonMixin.class);
        objectMapper.addMixIn(MathFunction.class, MathFunctionJsonMixin.class);
        objectMapper.addMixIn(TrapezoidFunction.class, TrapezoidFunctionJsonMixin.class);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(String.valueOf(getResources().getText(R.string.url))).addConverterFactory(JacksonConverterFactory.create(objectMapper)).build();
        weatherForecastBundleRepository = retrofit.create(WeatherForecastBundleRepository.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWeatherForecastBundle();
    }

    protected void injectViews(@ViewById(R.id.locationText) TextView locationTextView,
                               @ViewById(R.id.dateText) TextView dateTextView,
                               @ViewById(R.id.currentTimeText) TextView currentTimeTextView) {
        rootView = findViewById(R.id.rootView);

        this.locationTextView = locationTextView;
        this.dateTextView = dateTextView;
        this.currentTimeTextView = currentTimeTextView;
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
                //BEN_REVIEW : Une chance que je demandais pas de le faire...
            }

        } catch (IOException e) {
            e.printStackTrace();
            //Handle error
        }
    }

    @UiThread
    protected void setInterface() {
        ActivityMainBinding binding = ActivityMainBinding.bind(rootView);

        //BEN_REVIEW : C'étais tu vraiment nécessaire ? Je crois pas...Me voir
        for (WeatherForecast weatherForecast : weatherForecastBundle) {
            weatherForecasts.add(weatherForecast);
        }

        //BEN_INFO : Mis un time provider plus rapide pour tester.
        timedUtcTimeProvider = new FasterTimeProvider(new Handler(), MILLIS_DELAY, 240);
        timedUtcTimeProvider.start();
        //BEN_CORRECTION  : OH NON.....Je vois que vous n'avez vraiment pas saisi le but du DataBinding. Vous n'avez pas compris non plus comment fonctionne
        //                  la couche Modèle que je vous ai donné. Si tel est le cas, pourquoi ne pas être venu me voir ? Je mord pas...
        timedUtcTimeProvider.addTimeListener(new TimeProvider.TimeListener() {
            @Override
            public void onTimeChanged(TimeProvider eventSource) {
                UpdateView();
            }
        });

        //BEN_CORRECTION : DataBinding non utilisé...ça sert justement à éviter cela!
        locationTextView.setText(weatherForecastBundle.getLocationName());

        binding.setLiveWeather(new LiveWeatherViewModel(new LiveWeather(weatherForecastBundle, timedUtcTimeProvider)));
        binding.setForecastBundle(new WeatherForecastBundleViewModel(weatherForecastBundle));
        binding.setForecastLayoutId(R.layout.item_weather);
        binding.setForecastLayoutVariableId(BR.forecast);

        UpdateView();
    }

    //BEN_CORRECTION : Ça, ça devrait être dans la couche ViewModel, en plus d'être un BindingAdapter.
    public String getCurrentTime(TimeProvider timedUtcTimeProvider) {
        Date date = new Date(timedUtcTimeProvider.getCurrentTimeInSeconds() * SECONDS_TO_MILLIS);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", dateTextView.getTextLocale());

        return simpleDateFormat.format(date);

    }

    //BEN_CORRECTION : Idem.
    public String getCurrentDay() {
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(this);

        //BEN_REVIEW : Vous avez passé du temps la dessus pour rien...C'étais pas demandé
        String restOfDate = dateFormat.format(date);
        int indexOfFirstLetterOfMonth = restOfDate.indexOf(' ') + 1;
        restOfDate = restOfDate.substring(0, indexOfFirstLetterOfMonth) +
                restOfDate.substring(indexOfFirstLetterOfMonth, indexOfFirstLetterOfMonth+1).toUpperCase() +
                restOfDate.substring(indexOfFirstLetterOfMonth+1);

        return (new WeatherForecastViewModel(weatherForecasts.get(0))).getDayOfTheWeek(date) + ", " + restOfDate;
    }

    private void UpdateView(){
        for (WeatherForecast weatherForecast : weatherForecasts) {
            if (weatherForecast.canGetTemperatureAt(timedUtcTimeProvider.getCurrentTimeInSeconds())) {
                //BEN_CORRECTION : Euh....et le DataBinding ? Ça sert justement à ça!
                currentTimeTextView.setText(getCurrentTime(timedUtcTimeProvider));
                //BEN_CORRECTION : Idem.
                dateTextView.setText(getCurrentDay());
                break;
            }
        }
    }

}

