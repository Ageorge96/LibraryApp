package com.example.library;

import static com.example.library.MainActivity.getUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class WishlistActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Button addBtn, promoteBtn, removeBtn;
    ImageButton binImBtn;
    TextView countTextView;
    ListView wishlistView;

    Book selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        initWidgets();
        loadFromDBToMemory();
        setWishAdapter();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setTitle("Wishlist");
        getSupportActionBar().setHomeButtonEnabled(true);

        displayName();

        countTextView.setText(String.valueOf(Book.wishArrayList.size()));



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishlistActivity.this, AddToWishlistActivity.class);
                startActivity(intent);
            }
        });

        promoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected == null) {
                    Toast.makeText(WishlistActivity.this, "Please select a book for promotion first", Toast.LENGTH_SHORT).show();
                } else {

                    SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(WishlistActivity.this);


                    sqLiteManager.addBookToLirary(selected);
                    sqLiteManager.removeBookFromDatabase(selected, getString(R.string.wishlist_table));

                    finish();
                    startActivity(getIntent());
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binImBtn.getVisibility() == View.GONE) {
                    binImBtn.setVisibility(View.VISIBLE);
                    wishlistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    Toast.makeText(WishlistActivity.this, R.string.remove_hint, Toast.LENGTH_SHORT).show();
                } else {
                    binImBtn.setVisibility(View.GONE);
                    wishlistView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                }


            }
        });

        binImBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checked = wishlistView.getCheckedItemPositions();

                if (checked.size() == 0) {
                    Toast.makeText(WishlistActivity.this, "Make sure at least one book has been select", Toast.LENGTH_SHORT).show();
                    binImBtn.setVisibility(View.GONE);
                } else {



                    SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(getApplicationContext());

                    for (int i = 0; i < wishlistView.getCount(); i++) {
                        if (checked.get(i)) {

                            Log.i("TAG", "onClick: here");
                            sqLiteManager.removeBookFromDatabase((Book) wishlistView.getItemAtPosition(i), getString(R.string.wishlist_table));
                        }
                    }


                    finish();
                    startActivity(getIntent());
                }
            }
        });

        wishlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // assign book to selected variable when selected in listview
                selected = Book.wishArrayList.get(position);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(WishlistActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else if (itemId == R.id.nav_library) {
                    Intent intent = new Intent(WishlistActivity.this, LibraryActivity.class);
                    finish();
                    startActivity(intent);
                } else if (itemId == R.id.nav_profile) {
                    Toast.makeText(WishlistActivity.this, "Profile changes must be made in Home page", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void initWidgets() {

        drawerLayout = findViewById(R.id.drawerLayout_wishlist);
        navigationView = findViewById(R.id.navView_wishlist);
        toolbar = findViewById(R.id.toolbar_wishlist);
        setSupportActionBar(toolbar);

        addBtn = findViewById(R.id.btn_wishlist_add);
        promoteBtn = findViewById(R.id.btn_wishlist_promote);
        removeBtn = findViewById(R.id.btn_wishlist_remove);
        binImBtn = findViewById(R.id.imagebtn_wishlist_bin);

        countTextView = findViewById(R.id.txt_wishlist_bookNum);

        wishlistView = findViewById(R.id.listView_wishlist);
    }

    private void displayName() {
        View headerView = navigationView.getHeaderView(0);
        EditText usernameEditText = headerView.findViewById(R.id.txt_nav_username);

        String contextUsername = getUsername(this);


        usernameEditText.setText(contextUsername);
    }

    private void loadFromDBToMemory() {

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(WishlistActivity.this);
        sqLiteManager.populateWishlist();
    }

    private void setWishAdapter() {

        WishAdapter wishAdapter = new WishAdapter(getApplicationContext(), Book.wishArrayList);
        wishlistView.setAdapter(wishAdapter);
    }

    private boolean returnFromRemoveSelection() {

        if (binImBtn.isShown()) {
            binImBtn.setVisibility(View.GONE);

            // return list to single selection mode
            wishlistView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // clear list view selected item/s
            wishlistView.setItemChecked(-1, true);
            // clear selected book variable
            selected = null;

            return true;

        }

        return false;
    }

    @Override
    public void onBackPressed() {

        // close drawer if it is open
        //  else return activity to normal state if removal state is active and the back button is pressed
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (returnFromRemoveSelection()) {

        } else {
            Intent intent = new Intent(WishlistActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countTextView.setText(String.valueOf(Book.wishArrayList.size()));
        setWishAdapter();
    }


}