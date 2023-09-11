package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FullBookDetailsActivity extends AppCompatActivity {

    TextView titleTextView, authorTextView, seriesTextView, genreTextView, pagesTextView, statusTextView, bookmarkTextView, descriptionTextView;
    ImageView coverImageView;

    Button editBookBtn, addCoverBtn;

    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_book_details);
        initWidget();
        displayBook();

        editBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FullBookDetailsActivity.this, EditBookActivity.class);

                getBookToEdit(intent);
                startActivity(intent);
            }
        });
    }

    private void displayBook() {

        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        book = sqLiteManager.getAllDetails(title, author);

        titleTextView.setText(title);
        authorTextView.setText(author);
        seriesTextView.setText(book.getSeries());
        genreTextView.setText(book.getGenre());
        pagesTextView.setText(book.getPages());
        statusTextView.setText(book.getStatus());
        bookmarkTextView.setText(book.getBookmark());
        descriptionTextView.setText(book.getDescription());

        new DownloadURLImage((ImageView) coverImageView).execute(book.getCover());
    }

    private void initWidget() {
        titleTextView = findViewById(R.id.txt_bookDetails_title);
        authorTextView = findViewById(R.id.txt_bookDetails_bookAuthor);
        seriesTextView = findViewById(R.id.txt_bookDetails_bookSeries);
        genreTextView = findViewById(R.id.txt_bookDetails_bookGenre);
        pagesTextView = findViewById(R.id.txt_bookDetails_bookPages);
        statusTextView = findViewById(R.id.txt_bookDetails_bookStatus);
        bookmarkTextView = findViewById(R.id.txt_bookDetails_bookBookmark);
        descriptionTextView = findViewById(R.id.txt_bookDetails_bookDescription);


        coverImageView = findViewById(R.id.image_bookDetails_cover);

        editBookBtn = findViewById(R.id.btn_bookDetails_editBook);
        addCoverBtn = findViewById(R.id.btn_bookDetails_addCover);
    }

    private Intent getBookToEdit(Intent i) {

        String editTitle = book.getTitle();
        String editAuthor = book.getAuthor();
        String editSeries = book.getSeries();
        String editGenre = book.getGenre();
        String editPages = book.getPages();
        String editStatus = book.getStatus();
        String editBookmark = book.getBookmark();





        i.putExtra("Title", editTitle);
        i.putExtra("Author", editAuthor);
        i.putExtra("Series", editSeries);
        i.putExtra("Genre", editGenre);
        i.putExtra("Pages", editPages);
        i.putExtra("Status", editStatus);
        i.putExtra("Bookmark", editBookmark);

        return i;
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayBook();
    }
}