package com.user.getweatherusingspeechapp.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.user.getweatherusingspeechapp.repositories.WeatherRepository;

public class WeatherViewModelProviderFactory implements ViewModelProvider.Factory {
    private final WeatherRepository weatherRepository;

    public WeatherViewModelProviderFactory(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WeatherViewModel.class)) {
            return (T) new WeatherViewModel(weatherRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
