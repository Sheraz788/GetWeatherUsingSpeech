package com.user.getweatherusingspeechapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Toast;

import com.user.getweatherusingspeechapp.databinding.ActivityMainBinding;
import com.user.getweatherusingspeechapp.datasource.ApiClient;
import com.user.getweatherusingspeechapp.datasource.WeatherApiService;
import com.user.getweatherusingspeechapp.models.WeatherData;
import com.user.getweatherusingspeechapp.repositories.WeatherRepository;
import com.user.getweatherusingspeechapp.viewmodel.WeatherViewModel;
import com.user.getweatherusingspeechapp.viewmodel.WeatherViewModelProviderFactory;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int SPEECH_REQUEST_CODE = 1;
    private SpeechRecognizer speechRecognizer;

    private WeatherViewModel weatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //initialize classes
        Retrofit apiClient = ApiClient.getInstance();
        WeatherApiService weatherApiService = apiClient.create(WeatherApiService.class);

        WeatherRepository weatherRepository = new WeatherRepository(weatherApiService);

        weatherViewModel = new ViewModelProvider(this, new WeatherViewModelProviderFactory(weatherRepository)).get(WeatherViewModel.class);

        setClickListeners();
        observers();
    }

    private void setClickListeners(){

        binding.microphoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

    }

    private void observers(){

        weatherViewModel.getWeatherData().observe(this, new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                binding.progressBar.setVisibility(View.GONE);
                binding.weatherDescriptionTv.setText(weatherData.getWeatherDescription());
                binding.currentTempTv.setText(weatherData.getTemperature() +"°C");
            }
        });

        weatherViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error while fetching weather", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");

        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String spokenText = results.get(0);

                //call weather api here

                binding.cityNameTv.setText("Getting current weather for " + spokenText);
                weatherViewModel.searchWeather(spokenText);
                binding.progressBar.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

}