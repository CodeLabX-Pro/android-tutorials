package com.example.weatherapp;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Asks OpenWeather for the current weather in a city.
 * OkHttp downloads in the background; the answer is passed back on the main thread, ready to show.
 */
final class WeatherClient {
    interface Listener {
        void onWeather(Weather weather);

        void onError(@StringRes int message);
    }

    /** One client for the whole app, so connections are reused. */
    private final OkHttpClient http = new OkHttpClient();
    private final Handler mainThread = new Handler(Looper.getMainLooper());
    private final String apiKey;

    WeatherClient(String apiKey) {
        this.apiKey = apiKey;
    }

    /** Starts the request and returns it, so it can be cancelled. */
    Call fetch(String city, Listener listener) {
        // HttpUrl encodes the city safely, so names with spaces such as "New York" work.
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.openweathermap.org")
                .addPathSegments("data/2.5/weather")
                .addQueryParameter("q", city)
                .addQueryParameter("units", "metric")
                .addQueryParameter("appid", apiKey)
                .build();
        Call call = http.newCall(new Request.Builder().url(url).build());
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (!call.isCanceled()) error(R.string.error_network);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                // try-with-resources closes the reply, even if reading it fails.
                try (ResponseBody body = response.body()) {
                    if (response.code() == 401) error(R.string.error_key);
                    else if (response.code() == 404) error(R.string.error_city);
                    else if (!response.isSuccessful()) error(R.string.error_server);
                    else {
                        Weather weather = Weather.fromJson(body.string());
                        mainThread.post(() -> listener.onWeather(weather));
                    }
                } catch (IOException | JSONException e) {
                    error(R.string.error_server);
                }
            }

            private void error(@StringRes int message) {
                mainThread.post(() -> listener.onError(message));
            }
        });
        return call;
    }
}
