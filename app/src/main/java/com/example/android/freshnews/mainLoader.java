package com.example.android.freshnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

import static com.example.android.freshnews.QueryUtil.fetchingData;

public class mainLoader extends AsyncTaskLoader<ArrayList<News>> {
    private String mUrl;

    mainLoader(Context context, String url) {
        super(context);
        mUrl = url;


    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        return fetchingData(mUrl);
    }
}
