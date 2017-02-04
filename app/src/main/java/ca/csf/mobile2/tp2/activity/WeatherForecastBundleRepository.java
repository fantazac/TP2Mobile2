package ca.csf.mobile2.tp2.activity;

import ca.csf.mobile2.tp2.meteo.WeatherForecastBundle;
import retrofit2.Call;
import retrofit2.http.GET;

//BEN_CORRECTION : Pas dans le bon package.
public interface WeatherForecastBundleRepository {

    @GET("/weather/now.json")
    Call<WeatherForecastBundle> retrieve();

}
