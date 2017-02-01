package ca.csf.mobile2.tp2.activity;

import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WeatherForecastBundleRepository {

    @GET("/weather/random/now.json")
    Call<WeatherForecastBundle> retrieve();

}
