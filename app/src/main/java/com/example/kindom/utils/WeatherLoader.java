package com.example.kindom.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.net.URL;
import java.util.ArrayList;

/**
 * Asynchronously load data about weather from Data.gov.sg
 */
public class WeatherLoader extends AsyncTaskLoader<ArrayList<String>> {

    private String region;

    public WeatherLoader(@NonNull Context context, String region) {
        super(context);
        this.region = region;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<String> loadInBackground() {
        // Create URL objects
        URL weatherUrl = WeatherQuery.createUrl();
        URL psiURL = PsiQuery.createUrl();

        // Perform HTTP requests to the URL and return a JSON response
        String weatherJsonResponse = Connection.makeHttpRequest(weatherUrl);
        String psiJsonResponse = Connection.makeHttpRequest(psiURL);

        // Extract weather information from the JSON response
        ArrayList<String> responses = new ArrayList<>();

        ArrayList<String> weatherResponse = WeatherQuery.parseJsonResponse(weatherJsonResponse, region);
        responses.add(weatherResponse.get(0));
        responses.add(weatherResponse.get(1));

        String psiResponse = PsiQuery.parseJsonResponse(psiJsonResponse, region);
        responses.add(psiResponse);

        return responses;
    }
}