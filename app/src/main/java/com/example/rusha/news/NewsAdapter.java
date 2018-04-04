package com.example.rusha.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rusha on 5/24/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> newsItems) {
        super(context, 0, newsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }
        // Get the {@link NewsItem} object located at this position in the list
        News currentNewsItem = getItem(position);

        // Find the TextView in the book_list_item.xml layout with the ID book_title.
        TextView titleTextView = (TextView) convertView.findViewById(R.id.news);
        // Get the Title from the currentNewsItem object and set this text on the TextView.
        titleTextView.setText(currentNewsItem.getWebtitle());

        // Find the TextView in the book_list_item.xml layout with the ID book_author.
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
        // Get the Author from the currentNewsItem object and set this text on the TextView.
        dateTextView.setText(currentNewsItem.getDate());

        // Find the TextView in the book_list_item.xml layout with the ID book_page_count.
        TextView secTextView = (TextView) convertView.findViewById(R.id.section);
        // Get the Page Count from the currentNewsItem object and set this text on the TextView.
        secTextView.setText(currentNewsItem.getSecname());
        ImageView image = (ImageView) convertView.findViewById(R.id.imagelist);
        if (currentNewsItem.getThumbnail() != null) {
            Picasso.with(getContext()).load(currentNewsItem.getThumbnail()).into(image);
        }

        return convertView;
    }

}
