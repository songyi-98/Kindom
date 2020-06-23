package com.example.kindom.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.net.URL;

/**
 * Asynchronously load data about block number from OneMap API
 */
public class AddressLoader extends AsyncTaskLoader<Integer> {

    private int postalCode;

    public AddressLoader(@NonNull Context context, int postalCode) {
        super(context);
        this.postalCode = postalCode;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        // Create URL object
        URL url = AddressQuery.createUrl(postalCode);

        // Perform HTTP request to the URL and return a JSON response
        String jsonResponse = AddressQuery.makeHttpRequest(url);

        // Extract block number from the JSON response
        return AddressQuery.parseJsonResponse(jsonResponse);
    }
}