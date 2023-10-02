package com.user.getweatherusingspeechapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.user.getweatherusingspeechapp.models.WeatherData;
import com.user.getweatherusingspeechapp.repositories.WeatherRepository;

public class WeatherViewModel extends ViewModel {
    private final WeatherRepository weatherRepository;
    private final MutableLiveData<WeatherData> weatherData = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public WeatherViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void searchWeather(String location) {
        weatherRepository.getWeatherData(location, new WeatherRepository.Callback() {
            @Override
            public void onSuccess(WeatherData data) {
                weatherData.postValue(data);
            }

            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
            }
        });
    }
}