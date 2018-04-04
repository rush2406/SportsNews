package com.example.rusha.news;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rusha on 5/24/2017.
 */

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<News>> {


    NewsAdapter adapter;
    private static final int BOOK_LOADER_ID = 1;
    ProgressBar bar;
    private TextView mEmptyStateTextView;


    private String API_INITIAL_QUERY = "http://content.guardianapis.com/sport?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);
        ListView listView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.textview);
        bar = (ProgressBar) findViewById(R.id.loading);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current book that was clicked on
                News currentNewsItem = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookURL = Uri.parse(currentNewsItem.getWeburl());

                // Create new intent to view the book's URL
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookURL);

                // Start the intent
                startActivity(websiteIntent);
            }
        });

    }


    public String returnFinalQuery(String s) {

        if (s.equalsIgnoreCase("football"))
            API_INITIAL_QUERY = "http://content.guardianapis.com/football?";
        Uri baseUri = Uri.parse(API_INITIAL_QUERY);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(s).appendQueryParameter("order-by", "newest")
                .appendQueryParameter("show-fields", "thumbnail")
                .appendQueryParameter("api-key", "test");
        String url = uriBuilder.toString();
        return url;

    }


    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, returnFinalQuery(MainActivity.result));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        adapter.clear();
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        } else {
            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected)
                mEmptyStateTextView.setText(getString(R.string.error));
            else
                mEmptyStateTextView.setText(getString(R.string.connect));
        }

        bar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        adapter.clear();
    }


}
