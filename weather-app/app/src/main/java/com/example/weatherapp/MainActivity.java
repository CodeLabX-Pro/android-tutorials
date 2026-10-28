package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.Call;

/**
 * Search a city and see its weather. The last city you searched opens next time.
 */
public class MainActivity extends AppCompatActivity {
    private static final String DEFAULT_CITY = "Mumbai";

    private WeatherClient client;
    private Call request;
    private SharedPreferences prefs;

    private EditText cityInput;
    private View progress, content;
    private TextView error, city, temperature, description, feelsLike, humidity, wind;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        cityInput = findViewById(R.id.cityInput);
        progress = findViewById(R.id.progress);
        content = findViewById(R.id.content);
        error = findViewById(R.id.textError);
        city = findViewById(R.id.textCity);
        temperature = findViewById(R.id.textTemperature);
        description = findViewById(R.id.textDescription);
        feelsLike = findViewById(R.id.textFeelsLike);
        humidity = findViewById(R.id.textHumidity);
        wind = findViewById(R.id.textWind);
        icon = findViewById(R.id.weatherIcon);

        client = new WeatherClient(BuildConfig.OPENWEATHER_API_KEY);
        prefs = getSharedPreferences("weather", MODE_PRIVATE);

        findViewById(R.id.btnSearch).setOnClickListener(v -> search());
        // The keyboard's search key works too.
        cityInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_SEARCH) return false;
            search();
            return true;
        });

        if (BuildConfig.OPENWEATHER_API_KEY.isEmpty()) {
            showError(R.string.error_no_key);
        } else {
            load(prefs.getString("city", DEFAULT_CITY));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (request != null) request.cancel();
    }

    private void search() {
        String name = cityInput.getText().toString().trim();
        if (name.isEmpty()) {
            cityInput.setError(getString(R.string.error_empty));
            return;
        }
        WindowCompat.getInsetsController(getWindow(), cityInput).hide(WindowInsetsCompat.Type.ime());
        load(name);
    }

    private void load(String name) {
        if (request != null) request.cancel();
        progress.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        request = client.fetch(name, new WeatherClient.Listener() {
            @Override
            public void onWeather(Weather weather) {
                progress.setVisibility(View.GONE);
                show(weather);
                prefs.edit().putString("city", weather.city).apply();
            }

            @Override
            public void onError(int message) {
                showError(message);
            }
        });
    }

    private void show(Weather weather) {
        city.setText(weather.city);
        temperature.setText(getString(R.string.degrees, weather.temperature));
        description.setText(weather.description);
        feelsLike.setText(getString(R.string.degrees, weather.feelsLike));
        humidity.setText(getString(R.string.percent, weather.humidity));
        wind.setText(getString(R.string.kmh, weather.windKmh));
        icon.setImageResource(iconFor(weather.icon));
        content.setVisibility(View.VISIBLE);
    }

    private void showError(int message) {
        progress.setVisibility(View.GONE);
        error.setText(message);
        error.setVisibility(View.VISIBLE);
    }

    /** OpenWeather icon codes start with two digits that name the sky, such as 01 for clear and 10 for rain. */
    static int iconFor(String code) {
        switch (code.substring(0, 2)) {
            case "01": return R.drawable.ic_sun;
            case "02": return R.drawable.ic_partly_cloudy;
            case "09":
            case "10": return R.drawable.ic_rain;
            case "11": return R.drawable.ic_storm;
            case "13": return R.drawable.ic_snow;
            case "50": return R.drawable.ic_mist;
            default: return R.drawable.ic_cloud;
        }
    }
}
