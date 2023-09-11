package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WishlistActivity extends AppCompatActivity {

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

                    setWishAdapter();
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
    }

    private void initWidgets() {

        addBtn = findViewById(R.id.btn_wishlist_add);
        promoteBtn = findViewById(R.id.btn_wishlist_promote);
        removeBtn = findViewById(R.id.btn_wishlist_remove);
        binImBtn = findViewById(R.id.imagebtn_wishlist_bin);

        countTextView = findViewById(R.id.txt_wishlist_bookNum);

        wishlistView = findViewById(R.id.listView_wishlist);
    }

    private void loadFromDBToMemory() {

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(WishlistActivity.this);
        sqLiteManager.populateWishlist();
    }

    private void setWishAdapter() {

        WishAdapter wishAdapter = new WishAdapter(getApplicationContext(), Book.wishArrayList);
        wishlistView.setAdapter(wishAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countTextView.setText(String.valueOf(Book.wishArrayList.size()));
        setWishAdapter();
    }


}