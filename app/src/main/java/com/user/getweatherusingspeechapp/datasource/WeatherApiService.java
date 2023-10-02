package com.user.getweatherusingspeechapp.datasource;

import com.user.getweatherusingspeechapp.models.WeatherDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("weather?")
    Call<WeatherDataResponse> getWeatherData(@Query("q") String location, @Query("appid") String appid);
}
