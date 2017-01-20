package ca.csf.mobile2.tp2.activity;

import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Alexandre on 2017-01-20.
 */

public interface WeatherForecastBundleRepository {

    @GET("/weather/random/now.json")
    Call<WeatherForecastBundle> retrieve();

}
