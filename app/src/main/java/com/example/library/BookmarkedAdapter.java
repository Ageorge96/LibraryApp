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

public class BookmarkedAdapter extends ArrayAdapter<Book> {


    public BookmarkedAdapter(Context context, List<Book> book) {
        super(context, 0, book);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Book book = getItem(position);

        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_bookmarked, parent, false);

        TextView title = convertView.findViewById(R.id.txt_homeBCell_title);
        TextView author = convertView.findViewById(R.id.txt_homeBCell_author);
        TextView bookmark = convertView.findViewById(R.id.txt_homeBCell_bookmark);

        ImageView cover = convertView.findViewById(R.id.img_homeBCell_cover);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        bookmark.setText(book.getBookmark());

        new DownloadURLImage((ImageView) cover).execute(book.getCover());


        return convertView;
    }
}
