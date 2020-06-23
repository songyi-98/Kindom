package com.example.kindom.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
            url =new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String makeHttpRequest(URL url) {
        HttpURLConnection connection = null;
        InputStream inputStream =null;
        String jsonResponse = "";

        // If the url is null, return early
        if (url == null) {
            return jsonResponse;
        }

        try {
            // Set up and perform HTTP request
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.connect();

            // Check if HTTP request is successful
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  // enable efficient reading of text
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
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