package com.example.android.freshnews;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.ArrayList;

public final class QueryUtil {
    private static URL createUrl(String requestedURL) {
        URL url = null;
        try {
            url = new URL(requestedURL);
        } catch (MalformedURLException exception) {
            Log.e(LOG, "Error with creating URL ", exception);
        }
        return url;
    }

    public static ArrayList<String[]> fetchingData(String url) {
        ArrayList<String[]> arrayList;
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(createUrl(url));
        } catch (IOException e) {
            Log.e(LOG, "Error closing input stream", e);
        }
        arrayList = extractNews(jsonResponse);
        return arrayList;
    }

    private static final String LOG = QueryUtil.class.getSimpleName();

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG, "Problem retrieving the news JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return jsonResponse;
        }
    }


    private static String readFromStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    private static ArrayList<String[]> extractNews(String jsonRes) {

        ArrayList<String[]> news = new ArrayList<>();

        if (TextUtils.isEmpty(jsonRes)) {
            return null;
        }
        try {

            JSONObject root = new JSONObject(jsonRes);
            JSONObject jsonObject = root.getJSONObject("response");
            JSONArray results = jsonObject.optJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject object = results.getJSONObject(i);
                String firstString = object.getString("sectionName");
                String secondString = object.getString("webTitle");
                String pageURL = object.getString("webUrl");

                news.add(
                        new String[]{firstString, secondString, pageURL});

            }
        } catch (JSONException e) {
            Log.e("QueryUtil", "Problem parsing the news JSON results", e);
        }
        return news;
    }
}
