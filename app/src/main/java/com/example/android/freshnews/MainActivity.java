package com.example.android.freshnews;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String[]>> {
    public static ArrayList<String[]> resultList;
    public static final String requestedUrl =
            "http://content.guardianapis.com/search?section=technology&q=programming&api-key=50bf6d44-e9ce-401d-bb1a-2e38823403e6";
    ListView listView;
    private ArrayAdapter<String[]> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultList = new ArrayList<>();
        listView = findViewById(R.id.list);
        adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, new ArrayList<String[]>());
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<ArrayList<String[]>> onCreateLoader(int i, Bundle bundle) {
        return new mainLoader(MainActivity.this, requestedUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String[]>> loader, ArrayList<String[]> strings) {
        adapter.clear();
        if (strings != null && !strings.isEmpty()) {
            adapter.addAll(strings);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String[]>> loader) {
        adapter.clear();
    }
}
