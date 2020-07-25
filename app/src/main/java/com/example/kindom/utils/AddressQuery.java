package com.example.kindom.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Query block number from postal code
 */
public class AddressQuery {

    public static final String ONE_MAP_SEARCH = "https://developers.onemap.sg/commonapi/search?";
    public static final String SEARCH_VALUE = "searchVal";
    public static final String GEOMETRY = "returnGeom";
    public static final String GET_ADDRESS_DETAILS = "getAddrDetails";

    public static URL createUrl(int postalCode) {
        Uri builtUri = Uri.parse(ONE_MAP_SEARCH).buildUpon()
                .appendQueryParameter(SEARCH_VALUE, String.valueOf(postalCode))
                .appendQueryParameter(GEOMETRY, "N")
                .appendQueryParameter(GET_ADDRESS_DETAILS, "Y")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String parseJsonResponse(String jsonResponse) {
        String result = "invalid";
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonResponse);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            for (int i = resultsArray.length() - 1; i >= 0; i--) {
                result = resultsArray.getJSONObject(i).getString("BLK_NO");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}