package com.user.getweatherusingspeechapp.repositories;

import com.user.getweatherusingspeechapp.datasource.WeatherApiService;
import com.user.getweatherusingspeechapp.models.WeatherData;
import com.user.getweatherusingspeechapp.models.WeatherDataResponse;
import com.user.getweatherusingspeechapp.models.WeatherObject;
import com.user.getweatherusingspeechapp.utils.CommonUtilities;

import retrofit2.Call;
import retrofit2.Response;

public class WeatherRepository {
    private final WeatherApiService weatherApiService;
    CommonUtilities commonUtilities;

    public WeatherRepository(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
        commonUtilities = new CommonUtilities();
    }

    public void getWeatherData(String location, Callback callback) {
        // Perform API request and handle the response

        weatherApiService.getWeatherData(location, "e86b986fb158e202afe03cb94a0faf01").enqueue(new retrofit2.Callback<WeatherDataResponse>() {
            @Override
            public void onResponse(Call<WeatherDataResponse> call, Response<WeatherDataResponse> response) {
                if (response.isSuccessful()) {
                    WeatherDataResponse dataResponse = response.body();
                    if (dataResponse != null) {
                        WeatherData weatherData = transformResponseToWeatherData(dataResponse);
                        callback.onSuccess(weatherData);
                    } else {
                        callback.onError("No data available");
                    }
                } else {
                    callback.onError("Error fetching weather data");
                }
            }

            @Override
            public void onFailure(Call<WeatherDataResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private WeatherData transformResponseToWeatherData(WeatherDataResponse response) {
        // Converting  the response data to your WeatherData model

        WeatherData weatherData = new WeatherData();

        weatherData.setTemperature(commonUtilities.changeTemp(String.valueOf(response.getMain().getTemp())));
        WeatherObject weatherObject = (WeatherObject) response.getWeather().get(0);

        weatherData.setWeatherDescription(weatherObject.getDescription());

        return weatherData;
    }

    public interface Callback {
        void onSuccess(WeatherData data);

        void onError(String errorMsg);
    }
}