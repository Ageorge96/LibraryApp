package com.example.library;

import static com.example.library.Operations.isNumeric;
import static com.example.library.Operations.toTitleCase;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class AddToLibraryActivity extends AppCompatActivity {

    private EditText titleEditText, authorEditText, seriesEditText, pagesEditText, bmEditText, searchEditText;

    private CheckBox autoManualCheckBox;

    private Spinner genreSpinner;
    private Spinner statusSpinner;

    private ImageButton imbtnSearchBook;
    private Button btnAddBook;

    String cover, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvitity_addtolib);
        initWidgets();

        // if there is a book in the searchedbook variable of the Google data class
        // set it the object variables to corresponding edit text views
        if (GoogleData.searchedBook != null) returnSearchedBook();


        autoManualCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                // check if fragment switch checkbox is checked
                if (autoManualCheckBox.isChecked()) {
                    // switch instruction for checkbox text
                    autoManualCheckBox.setText(R.string.to_manual);

                    // start autobook fragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer_add_manualAuto, AutoBookFragment.class, null, "ManualFragment")
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                } else {
                    autoManualCheckBox.setText(R.string.to_auto);

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer_add_manualAuto, ManualBook.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                }
            }
        });



    }

    // set edit text fields to object fields from searched book
    private void returnSearchedBook() {
        titleEditText.setText(GoogleData.searchedBook.getGoogleTitle());
        authorEditText.setText(GoogleData.searchedBook.getGoogleAuthor());
        pagesEditText.setText(GoogleData.searchedBook.getPageCount());

    }

    private void initWidgets() {

        autoManualCheckBox = findViewById(R.id.checkbox_add_autoManual);

        titleEditText = findViewById(R.id.input_add_title);
        authorEditText = findViewById(R.id.input_add_author);
        seriesEditText = findViewById(R.id.input_add_series);
        genreSpinner = findViewById(R.id.spinner_add_genre);
        pagesEditText = findViewById(R.id.input_add_pages);
        bmEditText = findViewById(R.id.input_add_bookmark);
        statusSpinner = findViewById(R.id.spinner_add_status);

        searchEditText = findViewById(R.id.input_add_autoSearch);

        imbtnSearchBook = findViewById(R.id.imagebtn_add_searchBook);
        btnAddBook = findViewById(R.id.btn_addto_addBook);

    }


    public void addBook(View view) {

        // get device's detabase
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);


        String title = toTitleCase(String.valueOf(titleEditText.getText()));
        String author = toTitleCase(String.valueOf(authorEditText.getText()));
        String series = toTitleCase(String.valueOf(seriesEditText.getText()));
        String pages = String.valueOf(pagesEditText.getText());
        String bookmark = String.valueOf(bmEditText.getText());

        String genre = genreSpinner.getSelectedItem().toString();
        String status = statusSpinner.getSelectedItem().toString();

        cover = null;
        description = null;

        // if user has found book from search assign cover and description object variables to strings
        if (GoogleData.searchedBook != null) {
            GoogleData book = GoogleData.searchedBook;

            cover = book.getCover();
            description = book.getDescription();
        }


        // check input requirements are met
        if (title.equals("")) {
            Toast.makeText(AddToLibraryActivity.this,"Please provide a title", Toast.LENGTH_LONG).show();
            return;
        } else if (author.equals("")) {
            Toast.makeText(AddToLibraryActivity.this,"Please provide an author", Toast.LENGTH_LONG).show();
            return;
        } else if (!isNumeric(pages) && !pages.equals("")) {
            Toast.makeText(AddToLibraryActivity.this, "Number of pages must be an integer", Toast.LENGTH_LONG).show();
            return;
        } else if (genre.equals("Choose a genre")) {
            Toast.makeText(AddToLibraryActivity.this,"Please select a genre", Toast.LENGTH_LONG).show();
            return;
        } else if (status.equals("Select a status")) {
            Toast.makeText(AddToLibraryActivity.this,"Please select a status", Toast.LENGTH_LONG).show();
            return;
        } else if (!isNumeric(bookmark) && !bookmark.equals("")) {
            Toast.makeText(AddToLibraryActivity.this, "Bookmark must be an integer", Toast.LENGTH_LONG).show();
            return;
        }

        boolean repeatBook = false;

        // iterate through booklist
        for (int i = 0; i < Book.bookArrayList.size(); i++) {
            // compare searched book to books in existing database
            if (title.equals(Book.bookArrayList.get(i).getTitle()) && author.equals(Book.bookArrayList.get(i).getAuthor())) {

                // if a match is found set repeat to true and end loop
                repeatBook = true;
                break;


            }
        }

        // warn user that they are going to add a book that already exists in database
        if (repeatBook) {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_repeat_entry);

            Button yes = dialog.findViewById(R.id.btn_repeatDialog_yes);
            Button no = dialog.findViewById(R.id.btn_repeatDialog_no);

            dialog.show();


            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    Book newBook = new Book(title, author, series, genre, pages, status, bookmark, cover, null, description, null);
                    Book.bookArrayList.add(newBook);
                    sqLiteManager.addBookToLirary(newBook);

                    if (GoogleData.searchedBook != null) GoogleData.searchedBook = null;


                    finish();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            // initialise new book object if there is no repeat
            Book newBook = new Book(title, author, series, genre, pages, status, bookmark, cover, null, description, null);

            // add new book to existing book array list and database seperately
            Book.bookArrayList.add(newBook);
            sqLiteManager.addBookToLirary(newBook);

            // clear the information from the searched book variable
            if (GoogleData.searchedBook != null) GoogleData.searchedBook = null;


            finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (autoManualCheckBox.isChecked()) {
            autoManualCheckBox.setChecked(false);

            autoManualCheckBox.setText(R.string.to_auto);
        }
    }

}
