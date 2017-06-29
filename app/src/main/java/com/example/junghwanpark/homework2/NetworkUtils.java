package com.example.junghwanpark.homework2;

import android.net.Uri;

import com.example.junghwanpark.homework2.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by JungHwanPark on 6/28/2017.
 */

public class NetworkUtils {
    public static final String BASE_URL =
            "https://newsapi.org/v1/articles";
    public static final String PARAM_QUERY = "source";
    public static final String PARAM_SORT = "sortBy";
    public static final String PARAM_API = "apiKey";
    //https://api.github.com/search/repositories?q=hello&sort=stars

    public static URL makeURL(String searchQuery, String sortBy, String aKey) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, searchQuery)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_API, aKey).build();

        URL url = null;
        try {
            String urlString = uri.toString();
            //Log.d(TAG, "Url: " + urlString);
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);

            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException {
        ArrayList<NewsItem> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("articles");

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String author = item.getString("author");
            String title = item.getString("title");
            //System.out.println("author: " + author);
            String description = item.getString("description");
            String url = item.getString("url");
            String urlToImage = item.getString("urlToImage");
            String publishedAt = item.getString("publishedAt");
            NewsItem repo = new NewsItem(author, title, description, url, urlToImage, publishedAt);
            result.add(repo);
        }
        return result;
    }
}



