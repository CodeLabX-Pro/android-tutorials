package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

/** The current weather for one city, read from OpenWeather's JSON reply. */
final class Weather {
    final String city;
    final String description;
    /** OpenWeather's icon code, such as "01d" (clear sky, day) or "10n" (rain, night). */
    final String icon;
    final int temperature;
    final int feelsLike;
    final int humidity;
    final int windKmh;

    private Weather(String city, String description, String icon, int temperature, int feelsLike, int humidity, int windKmh) {
        this.city = city;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windKmh = windKmh;
    }

    /**
     * Reads the parts of the reply we show. With units=metric, temperatures are in °C and wind is in metres per second.
     * Example: {"name":"Mumbai","main":{"temp":29.4,"feels_like":33.1,"humidity":70},"wind":{"speed":4.1},
     * "weather":[{"description":"haze","icon":"50d"}]}
     */
    static Weather fromJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONObject main = root.getJSONObject("main");
        JSONObject sky = root.getJSONArray("weather").getJSONObject(0);
        String description = sky.getString("description");
        return new Weather(
                root.getString("name"),
                description.isEmpty() ? description : Character.toUpperCase(description.charAt(0)) + description.substring(1),
                sky.getString("icon"),
                (int) Math.round(main.getDouble("temp")),
                (int) Math.round(main.getDouble("feels_like")),
                main.getInt("humidity"),
                (int) Math.round(root.getJSONObject("wind").getDouble("speed") * 3.6));
    }
}
