package com.example.library;

import static com.example.library.Operations.isNumeric;
import static com.example.library.Operations.toTitleCase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class EditBookActivity extends AppCompatActivity {

    private EditText titleEditText, authorEditText, seriesEditText, pagesEditText, bmEditText;


    Spinner genreSpinner;
    Spinner statusSpinner;

    Button btnSaveBook;

    Book book;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initWidgets();
        book = getBook();
        setBook(book);
    }


    private void initWidgets() {

        titleEditText = findViewById(R.id.input_edit_title);
        authorEditText = findViewById(R.id.input_edit_author);
        seriesEditText = findViewById(R.id.input_edit_series);
        genreSpinner = findViewById(R.id.spinner_edit_genre);
        pagesEditText = findViewById(R.id.input_edit_pages);
        statusSpinner = findViewById(R.id.spinner_edit_status);
        bmEditText = findViewById(R.id.input_edit_bookmark);

        btnSaveBook = findViewById(R.id.btn_edit_saveChanges);

    }

    // get book using intent extras
    private Book getBook() {
        // get device's database
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        String title = getIntent().getStringExtra("Title");
        String author = getIntent().getStringExtra("Author");

        return sqLiteManager.getAllDetails(title, author);
    }


    private void setBook(Book book) {


        titleEditText.setText(book.getTitle());
        authorEditText.setText(book.getAuthor());
        seriesEditText.setText(book.getSeries());
        pagesEditText.setText(book.getPages());
        bmEditText.setText(book.getBookmark());

        int genrePostion = Arrays.asList(getResources().getStringArray(R.array.genre)).indexOf(book.getGenre());
        int statusPostion = Arrays.asList(getResources().getStringArray(R.array.status)).indexOf(book.getStatus());



        String TAG = "Array";
        Log.i(TAG, "setBook: genre array: " + Arrays.asList(getResources().getStringArray(R.array.genre)));

        genreSpinner.setSelection(genrePostion);
        statusSpinner.setSelection(statusPostion);
    }

    public void saveChanges (View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        String title = book.getTitle();
        String author = book.getAuthor();

        String updatedTitle = toTitleCase(titleEditText.getText().toString());
        String updatedAuthor = toTitleCase(authorEditText.getText().toString());
        String updatedSeries = toTitleCase(seriesEditText.getText().toString());
        String updatedPages = pagesEditText.getText().toString();
        String updatedBookmark = bmEditText.getText().toString();

        if (updatedBookmark.equals("~No bookmark added")) {
            updatedBookmark = "";
        }

        String updatedGenre = genreSpinner.getSelectedItem().toString();
        String updatedStatus = statusSpinner.getSelectedItem().toString();

        String cover = book.getCover();
        String description = book.getDescription();

        if (updatedTitle.equals("")) {
            Toast.makeText(EditBookActivity.this,"Please input a title", Toast.LENGTH_LONG).show();
        } else if (updatedAuthor.equals("")) {
            Toast.makeText(EditBookActivity.this,"Please input an author", Toast.LENGTH_LONG).show();
        } else if (!isNumeric(updatedPages) && !updatedPages.equals("~No pages added")) {
            Toast.makeText(EditBookActivity.this, "Number of pages must be an integer", Toast.LENGTH_LONG).show();
        } else if (!isNumeric(updatedBookmark) && !updatedBookmark.equals("")) {
            Toast.makeText(EditBookActivity.this, "Bookmark must be an integer", Toast.LENGTH_LONG).show();
        } else {

            Book book = new Book(updatedTitle, updatedAuthor, updatedSeries, updatedGenre, updatedPages, updatedStatus, updatedBookmark, cover, null, description, null);

            sqLiteManager.updateBookDB(title, author, book);

            finish();
        }
    }

    public void refreshBook(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
