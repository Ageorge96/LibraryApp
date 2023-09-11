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

public class WishAdapter extends ArrayAdapter<Book> {


    public WishAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book book = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_wishlist, parent, false);

        TextView title = convertView.findViewById(R.id.txt_wishCell_title);
        TextView author = convertView.findViewById(R.id.txt_wishCell_author);
        TextView genre = convertView.findViewById(R.id.txt_wishCell_genre);
        TextView price = convertView.findViewById(R.id.txt_wishCell_price);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        genre.setText(book.getGenre());
        price.setText(book.getGooglePrice());

        ImageView cover = convertView.findViewById(R.id.img_wishCell_cover);
        new DownloadURLImage((ImageView) cover).execute(book.getCover());

        return convertView;
    }
}
