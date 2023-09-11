package com.example.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;


public class GoogleSearchAdapter extends ArrayAdapter<GoogleData> {


    public GoogleSearchAdapter(Context context, List<GoogleData> googleData) {
        super(context, 0, googleData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        GoogleData googleData = getItem(position);

        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.google_search_cell, parent, false);

        TextView title = convertView.findViewById(R.id.cell_google_title);
        TextView author = convertView.findViewById(R.id.cell_google_author);

        title.setText(googleData.getGoogleTitle());
        author.setText(googleData.getGoogleAuthor());


        return convertView;
    }

}
