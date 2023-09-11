package com.example.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HomeWishAdapter extends ArrayAdapter<Book> {

    public HomeWishAdapter(@NonNull Context context, List<Book> book) {
        super(context, 0, book);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Book book = getItem(position);

        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_home_wishlist, parent,false);

        TextView title = convertView.findViewById(R.id.txt_homeWCell_title);
        TextView author = convertView.findViewById(R.id.txt_homeWCell_author);
        TextView price = convertView.findViewById(R.id.txt_homeWCell_price);

        ImageView cover = convertView.findViewById(R.id.img_homeWCell_cover);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        price.setText(book.getGooglePrice());

        new DownloadURLImage((ImageView) cover).execute(book.getCover());

        return convertView;
    }
}
