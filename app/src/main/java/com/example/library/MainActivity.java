package com.example.library;

import static com.example.library.Operations.isNumeric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ListView bookListView, wishListView;
    TextView bookmarkMessageTextView, wishlistMessageTextView;
    Dialog dialog;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initWidget();
        loadFromMemoryToDB();
        setBookmarked();
        setWishlist();




        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayName();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {

                } else if (itemId == R.id.nav_library) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_wishlist) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_profile) {

                    profilePopup();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }


                return false;
            }
        });




    }

    private void loadFromMemoryToDB() {

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateBookList();
        sqLiteManager.populateWishlist();
    }

    private Toolbar initWidget() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bookListView = findViewById(R.id.listView_home_bookmarked);
        wishListView = findViewById(R.id.listView_home_wishlist);
        bookmarkMessageTextView = findViewById(R.id.txt_home_noBookmark);
        wishlistMessageTextView = findViewById(R.id.txt_home_noWish);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        return toolbar;
    }

    private void setWishlist() {

        List<Book> displayBooks = new ArrayList<>();

        if (Book.wishArrayList.size() == 0) {
            wishListView.setVisibility(View.INVISIBLE);
            wishlistMessageTextView.setText("Your wishlist is empty");
            wishlistMessageTextView.setVisibility(View.VISIBLE);
            return;
        } else if (wishListView.getVisibility() == View.GONE) wishListView.setVisibility(View.VISIBLE);

        for (int i = 0; i < 3; i++) {
            displayBooks.add(Book.wishArrayList.get(i));
        }

        HomeWishAdapter homeWishAdapter = new HomeWishAdapter(this, displayBooks);
        wishListView.setAdapter(homeWishAdapter);

    }

    private void setBookmarked() {

        List<Book> displayBook = new ArrayList<>();

        if (Book.bookArrayList.size() == 0) {
                bookListView.setVisibility(View.INVISIBLE);
                bookmarkMessageTextView.setText("You have no books in your library");
                bookmarkMessageTextView.setVisibility(View.VISIBLE);
                return;
        } else if (bookListView.getVisibility() == View.INVISIBLE) bookListView.setVisibility(View.VISIBLE);

        int highestBookmark = 0;
        int currentBook = -1;

        for (int i = 0; i < Book.bookArrayList.size(); ++i) {

            if (!isNumeric(Book.bookArrayList.get(i).getBookmark()) ) {
                continue;
            }

            int bookmark = Integer.valueOf(Book.bookArrayList.get(i).getBookmark());

            if (bookmark > highestBookmark) {
                currentBook = i;
            }
        }

        Log.i("TAG", "setBookmarked: Current book: " + currentBook);
        if (currentBook == -1) {

            bookListView.setVisibility(View.INVISIBLE);
            bookmarkMessageTextView.setText("You are not reading anything at the moment. " +
                    "Why don't you check you're library and start a new book?");
            bookmarkMessageTextView.setVisibility(View.VISIBLE);
            return;
        }

        displayBook.add(Book.bookArrayList.get(currentBook));

        BookmarkedAdapter bookmarkedAdapter = new BookmarkedAdapter(getApplicationContext(), displayBook);
        bookListView.setAdapter(bookmarkedAdapter);

    }


    private void profilePopup() {

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_profile);
        dialog.show();

        RadioGroup themeGroup = dialog.findViewById(R.id.radioG_profileDialog_theme);
        themeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("TAG", "onCheckedChanged: " + checkedId);
            }
        });
    }

    public void saveSettings(View view) {

        EditText username = dialog.findViewById(R.id.input_profile_userName);
        setUsername(this, username.getText().toString().strip());
        displayName();


        RadioGroup themeGroup = dialog.findViewById(R.id.radioG_profileDialog_theme);
        int checkedTheme = themeGroup.getCheckedRadioButtonId();

        Log.i("TAG", "saveSettings: " + checkedTheme);

        switch (checkedTheme) {
            case 1:
                getApplication().setTheme(R.style.Theme_ProfileOption2);
                break;
            case 2:
                getApplication().setTheme(R.style.Theme_ProfileOption2);
                break;
            case 3:
                getApplication().setTheme(R.style.Theme_ProfileOption2);
                break;
        }




        dialog.dismiss();
    }

    private void setUsername (Context context, String input) {
        SharedPreferences username = context.getSharedPreferences("com.example.library", 0);
        SharedPreferences.Editor editor = username.edit();
        editor.putString("username", input);
        editor.commit();
    }

    public String getUsername (Context context) {
        SharedPreferences username = context.getSharedPreferences("com.example.library", 0);

        return username.getString("username", "User");
    }

    private void displayName () {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        EditText usernameEditText = headerView.findViewById(R.id.txt_nav_username);

        String contextUsername = getUsername(this);


        usernameEditText.setText(contextUsername);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setBookmarked();
        setWishlist();
    }
}