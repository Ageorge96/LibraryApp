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

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book book = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_bookv2, parent, false);



        TextView title = convertView.findViewById(R.id.txt_libraryCell_title);
        TextView author = convertView.findViewById(R.id.txt_libraryCell_author);
        TextView genre = convertView.findViewById(R.id.txt_libraryCell_genre);
        TextView status = convertView.findViewById(R.id.txt_libraryCell_status);
        /*TextView series = convertView.findViewById(R.id.cell_series);

        TextView pages = convertView.findViewById(R.id.cell_pages);

        TextView bookmark = convertView.findViewById(R.id.cell_bookmark);*/

        ImageView cover = convertView.findViewById(R.id.image_libraryCell_cover);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        //series.setText(book.getSeries());
        genre.setText(book.getGenre());
        //pages.setText(book.getPages());
        status.setText(book.getStatus());
        //bookmark.setText(book.getBookmark());


        new DownloadURLImage((ImageView) cover).execute(book.getCover());


        return convertView;
    }
}
