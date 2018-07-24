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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String[]>> {
    public static ArrayList<String[]> resultList;
    public static final String requestedUrl =
            "https://content.guardianapis.com/search";
    //?section=technology%7Ceducation%7Cbreak-into-tech%7Cinfo&from-date=2017-01-01&page-size=30&q=programming%7Ccoding&api-key=
    public static String apiKey = BuildConfig.apiKey;
    ListView listView;
    private ArrayAdapter<String[]> adapter;
    private ProgressBar progressBar;
    private TextView CAUTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progBar);
        CAUTION = findViewById(R.id.caution);
        resultList = new ArrayList<>();
        listView = findViewById(R.id.list);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        adapter =
                new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, resultList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        convertView = super.getView(position, convertView, parent);
                        String[] currentString = resultList.get(position);
                        TextView textView1 = convertView.findViewById(android.R.id.text1);
                        TextView textView2 = convertView.findViewById(android.R.id.text2);
                        textView1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.textColorNewsTitle));
                        textView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.textColorNewsDetail));
                        textView1.setText(currentString[0]);
                        textView2.setText(currentString[1]);
                        return convertView;
                    }
                };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] urlString = resultList.get(i);
                Uri uri = Uri.parse(urlString[2]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        listView.setEmptyView(progressBar);

        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
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
    public Loader<ArrayList<String[]>> onCreateLoader(int i, Bundle bundle) {
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
        builder.appendQueryParameter("q", "programming|coding");
        builder.appendQueryParameter("section", "technology|education|break-into-tech|info");
        builder.appendQueryParameter("page-size", pageSize);
        builder.appendQueryParameter("order-by", orderBy);
        builder.appendQueryParameter("use-date", useDate);
        builder.appendQueryParameter("api-key", apiKey);

        return new mainLoader(MainActivity.this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String[]>> loader, ArrayList<String[]> strings) {
        CAUTION.setText(R.string.no_news);
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        if (strings != null && !strings.isEmpty()) {
            CAUTION.setVisibility(View.GONE);
            adapter.addAll(strings);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String[]>> loader) {
        CAUTION.setVisibility(View.GONE);
        adapter.clear();
    }
}
