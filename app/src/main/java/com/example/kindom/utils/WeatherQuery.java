package com.example.kindom.utils;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Query weather information
 */
public class WeatherQuery {

    public static final String SEARCH = "https://api.data.gov.sg/v1/environment/24-hour-weather-forecast?";
    public static final String DATE_TIME = "date_time";

    public static URL createUrl() {
        Uri builtUri = Uri.parse(SEARCH).buildUpon()
                .appendQueryParameter(DATE_TIME, CalendarHandler.getCurrentDateTime())
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static ArrayList<String> parseJsonResponse(String jsonResponse, String region) {
        ArrayList<String> result = new ArrayList<>();
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonResponse);
            JSONObject itemObject = jsonObject.getJSONArray("items").getJSONObject(0);

            String weather = itemObject
                    .getJSONArray("periods")
                    .getJSONObject(0)
                    .getJSONObject("regions")
                    .getString(region);
            result.add(weather);

            JSONObject temperatureObject = itemObject
                    .getJSONObject("general")
                    .getJSONObject("temperature");
            int low = temperatureObject.getInt("low");
            int high = temperatureObject.getInt("high");
            String temperature = low + " - " + high + " °C";
            result.add(temperature);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}