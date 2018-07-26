package com.example.android.freshnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter {
    NewsAdapter(@NonNull Context context, ArrayList<News> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        News currentNews = (News) getItem(position);
        TextView titleText = convertView.findViewById(R.id.news_title);
        titleText.setText(currentNews.getTitle());
        TextView sectionText = convertView.findViewById(R.id.section);
        sectionText.setText(currentNews.getSection());
        TextView authorText = convertView.findViewById(R.id.author);
        authorText.setText(currentNews.getAuthor());
        TextView dateText = convertView.findViewById(R.id.date);
        dateText.setText(currentNews.getDate());
        if ("" == authorText.getText()) {
            authorText.setVisibility(View.GONE);
        }
        return convertView;
    }
}
