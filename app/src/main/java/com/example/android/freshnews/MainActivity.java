package com.example.android.freshnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>>, Constant {
    private static final int LOADER_ID = 0;
    private static final String requestedUrl =
            "https://content.guardianapis.com/search";
    private NewsAdapter adapter;
    private ProgressBar progressBar;
    private TextView CAUTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progBar);
        CAUTION = findViewById(R.id.caution);
        ArrayList<News> resultList = new ArrayList<>();
        ListView listView = findViewById(R.id.list);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        adapter = new NewsAdapter(this, resultList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News currentNews = (News) adapter.getItem(i);
                Uri uri = Uri.parse(currentNews.getURL());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        listView.setEmptyView(progressBar);

        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            CAUTION.setText(R.string.no_internet);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = sharedPref.getString(
                getString(R.string.page_size_key),
                getString(R.string.page_size_default));
        String orderBy = sharedPref.getString(
                getString(R.string.order_by_key),
                getString(R.string.order_by_default));
        String useDate = sharedPref.getString(
                getString(R.string.use_date_key),
                getString(R.string.use_date_default));

        Uri uri = Uri.parse(requestedUrl);
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("q", q);
        builder.appendQueryParameter("section", section_const);
        builder.appendQueryParameter("page-size", pageSize);
        builder.appendQueryParameter("order-by", orderBy);
        builder.appendQueryParameter("use-date", useDate);
        builder.appendQueryParameter("show-tags", show_tags);
        builder.appendQueryParameter("api-key", BuildConfig.apiKey);


        return new mainLoader(MainActivity.this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> strings) {
        CAUTION.setText(R.string.no_news);
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        if (strings != null && !strings.isEmpty()) {
            CAUTION.setVisibility(View.GONE);
            adapter.addAll(strings);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        CAUTION.setVisibility(View.GONE);
        adapter.clear();
    }
}
