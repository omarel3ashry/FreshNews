package com.example.android.freshnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class mainLoader extends AsyncTaskLoader<ArrayList<String[]>> {
    String mUrl;

    public mainLoader(Context context,String url) {
        super(context);
        mUrl = url;

    }

    @Override
    public ArrayList<String[]> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        ArrayList<String[]> arrayList = QueryUtil.fetchingData(MainActivity.requestedUrl);
        return arrayList;
    }
}
