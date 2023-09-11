package com.example.library;

import static com.example.library.Operations.isCurrency;
import static com.example.library.Operations.isNumeric;
import static com.example.library.Operations.toTitleCase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddToWishlistActivity extends AppCompatActivity {

    EditText titleEditText, authorEditText, pagesEditText, priceEditText;
    Spinner genreSpinner;
    Button addBtn;
    CheckBox autoManualCheckBox;

    String cover, rating, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_wishlist);
        intiWidget();

        // if there is a book in the searchedbook variable of the Google data class
        // set it the object variables to corresponding edit text views
        if (GoogleData.searchedBook != null) returnSearchedBook();


        autoManualCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                // check if fragment switch checkbox is checked
                if (autoManualCheckBox.isChecked()) {
                    autoManualCheckBox.setText(R.string.to_manual);

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer_addToWish_manualAuto, AutoWishlistFragment.class, null, "ManualFragment")
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                } else {
                    autoManualCheckBox.setText(R.string.to_auto);

                    fragmentManager.popBackStack();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "addWish: run wish ");

                SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(AddToWishlistActivity.this);

                String title = toTitleCase(titleEditText.getText().toString()).strip();
                String author = toTitleCase(authorEditText.getText().toString()).strip();
                String pages = pagesEditText.getText().toString().strip();
                String price = priceEditText.getText().toString().strip();

                String genre = genreSpinner.getSelectedItem().toString();

                cover = null;
                rating = null;
                description = null;

                if (GoogleData.searchedBook != null) {

                    cover = GoogleData.searchedBook.getCover();
                    rating = GoogleData.searchedBook.getRating();
                    description = GoogleData.searchedBook.getDescription();
                }

                if (title.equals("")) {
                    Toast.makeText(AddToWishlistActivity.this, "Please provide a title", Toast.LENGTH_SHORT).show();
                    return;
                } else if (author.equals("")) {
                    Toast.makeText(AddToWishlistActivity.this, "please provide an author", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isNumeric(pages)) {
                    Toast.makeText(AddToWishlistActivity.this, "Number of pages must be an integer", Toast.LENGTH_SHORT).show();
                    return;
                } else if (genre.equals("Choose a genre")) {
                    Toast.makeText(AddToWishlistActivity.this, "please select a genre", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isCurrency(price)) {
                    Toast.makeText(AddToWishlistActivity.this, "Please provide a currency value", Toast.LENGTH_SHORT).show();
                    return;
                }


                Book newBook = new Book(title, author, null, genre, pages, null, null, cover, rating, description, price);
                Book.wishArrayList.add(newBook);
                sqLiteManager.addBookToWishlist(newBook);


                if (GoogleData.searchedBook != null) GoogleData.searchedBook = null;

                finish();
            }
        });
    }


    private void intiWidget() {

        titleEditText = findViewById(R.id.input_addToWish_title);
        authorEditText = findViewById(R.id.input_addToWish_author);
        pagesEditText = findViewById(R.id.input_addToWish_pages);
        priceEditText = findViewById(R.id.input_addToWish_price);

        genreSpinner = findViewById(R.id.spinner_addToWish_genre);

        autoManualCheckBox = findViewById(R.id.checkbox_addToWish_autoManual);

        addBtn = findViewById(R.id.btn_addToWish_addBook);
    }

    public void addWish(View view) {

        Log.i("TAG", "addWish: run wish ");

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        String title = titleEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String pages = pagesEditText.getText().toString();
        String price = priceEditText.getText().toString();

        String genre = genreSpinner.getSelectedItem().toString();

        cover = null;
        rating = null;
        description = null;

        if (GoogleData.searchedBook != null) {

            cover = GoogleData.searchedBook.getCover();
            rating = GoogleData.searchedBook.getRating();
            description = GoogleData.searchedBook.getDescription();
        }

        Book newBook = new Book(title, author, null, genre, pages, null, null, cover, rating, description, price);
        Book.wishArrayList.add(newBook);
        sqLiteManager.addBookToWishlist(newBook);

        Book.wishArrayList.add(newBook);

        if (GoogleData.searchedBook != null) GoogleData.searchedBook = null;

        finish();
    }

    private void returnSearchedBook() {
        GoogleData book = GoogleData.searchedBook;

        titleEditText.setText(book.getGoogleTitle());
        authorEditText.setText(book.getGoogleAuthor());
        pagesEditText.setText(book.getPageCount());
        priceEditText.setText(book.getGooglePrice());
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