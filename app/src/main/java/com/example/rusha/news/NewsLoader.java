package com.example.rusha.news;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by rusha on 5/24/2017.
 */

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    private String mUrl;

    public NewsLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        if (mUrl == null)
            return null;
        ArrayList<News> newItems = QueryUtils.fetchNewsData(mUrl);
        return newItems;
    }
}
