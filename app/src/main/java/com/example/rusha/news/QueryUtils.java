package com.example.rusha.news;

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
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rusha on 5/24/2017.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<News> fetchNewsData(String requestURL) {
        // Create URL object
        URL url = createURL(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Parse JSON string and create an {@ArrayList<NewsItem>} object
        ArrayList<News> newsitems = extractNewsData(jsonResponse);

        return newsitems;
    }

    public static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error Creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHTTPRequest(URL url) throws IOException {

        // If the url is empty, return early
        String jsonResponse = null;
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and
            // parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Google Newss JSON results", e);
        } finally {
            // Close connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputstream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();
        if (inputstream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputstream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = reader.readLine();
            }
        }
        return streamOutput.toString();
    }


    private static String formatDate(String rawDate) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("QueryUtils", "Error parsing JSON date: ", e);
            return "";
        }
    }


    private static ArrayList<News> extractNewsData(String newsDataJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsDataJSON)) {
            return null;
        }

        // Create empty ArrayList in which the parsed data will be added
        ArrayList<News> newsitems = new ArrayList<News>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Build a list of NewsItem Objects
            JSONObject rootObject = new JSONObject(newsDataJSON);
            JSONObject jsonResults = rootObject.getJSONObject("response");
            JSONArray resultsArray = jsonResults.getJSONArray("results");
            // Variables for JSON parsing
            String secname;
            String webtitle;
            String weburl;
            String pubdate;
            String date;
            String thumbnail;


            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentNews = resultsArray.getJSONObject(i);
                // Check if key "title" exists and if yes, return value
                if (currentNews.has("sectionName")) {
                    secname = currentNews.getString("sectionName");
                } else
                    secname = null;
                if (currentNews.has("webTitle")) {
                    webtitle = currentNews.getString("webTitle");
                } else
                    webtitle = null;
                if (currentNews.has("webUrl")) {
                    weburl = currentNews.getString("webUrl");
                } else
                    weburl = null;
                if (currentNews.has("webPublicationDate")) {
                    pubdate = currentNews.getString("webPublicationDate");
                    date = formatDate(pubdate);
                } else
                    date = null;
                JSONObject field = currentNews.getJSONObject("fields");
                if (field.has("thumbnail")) {
                    thumbnail = field.getString("thumbnail");
                } else
                    thumbnail = null;

                // Create the NewsItem object and add it to the ArrayList
                News newItem = new News(secname, webtitle, weburl, date, thumbnail);
                newsitems.add(newItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the NewsItem JSON results", e);
        }

        // Return the list of NewsItems
        return newsitems;
    }
}

