package com.example.kindom.utils;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Query PSI information
 */
public class PsiQuery {

    public static final String SEARCH = "https://api.data.gov.sg/v1/environment/psi?";
    public static final String DATE_TIME = "date_time";

    public static URL createUrl() {
        Uri builtUri = Uri.parse(SEARCH).buildUpon()
                .appendQueryParameter(DATE_TIME, CalendarHandler.getCurrentDateTime())
                .build();

        URL url = null;
        try {
            url =new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String parseJsonResponse(String jsonResponse, String region) {
        String result = "";
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonResponse);
            int psi = jsonObject
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getJSONObject("readings")
                    .getJSONObject("psi_twenty_four_hourly")
                    .getInt(region);
            result += psi;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}