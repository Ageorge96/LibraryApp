package com.example.library;

import static com.example.library.MainActivity.getUsername;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.Comparator;

public class LibraryActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    private ListView bookListView;


    TextView bookCount;

    Button btnAdd;
    Button btnEdit;
    Button btnRemove;
    Spinner sortSpinner, orderSpinner;

    ImageButton btnBin;

    Book selected;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        loadFromDBToMemory();
        initWidgets();
        setBookAdapter();
        displayName();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setTitle("Library");
        getSupportActionBar().setHomeButtonEnabled(true);

        // display the value of total books in arraylist (user's library)
        bookCount.setText(String.valueOf(Book.bookArrayList.size()));

        // move to add book activity when the add button is pressed
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to Add to library activity
                Intent intent = new Intent(LibraryActivity.this, AddToLibraryActivity.class);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if user has selected a book from the listview
                // selected variable is assigned when user clicks a book in the list view
                if (selected != null) {
                    // get book details for selected book
                    goToFullDetails();
                } else {
                    // inform the user that a book needs to be selected
                    Toast.makeText(LibraryActivity.this, "Select a book first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check activity state
                // if activity is in removal state, return activity to state prior to book removal state on button press
                // else switch activity to removal state
                if(returnFromRemoveSelection()){

                } else {

                    // set list view to allow for selection of multiple books
                    bookListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    // make bin icon visible
                    btnBin.setVisibility(View.VISIBLE);


                    // inform user on how to remove books from their library
                    Toast.makeText(LibraryActivity.this, R.string.remove_hint, Toast.LENGTH_LONG).show();

                }
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // on item selection run sort case
                switch (position) {
                    // if user selects default organise list based on sql database order
                    case 1:
                        // load list from sql databse
                        loadFromDBToMemory();
                        setBookAdapter();
                        break;
                    // if title is selected organise list alphabetically by title
                    case 2:
                        Book.bookArrayList.sort(Comparator.comparing(Book::getTitle));
                        setBookAdapter();
                        break;
                    // if author is selected organise list alphabetically by author
                    case 3:
                        Book.bookArrayList.sort(Comparator.comparing(Book::getAuthor));
                        setBookAdapter();
                        break;
                    // if series is selected organise list alphabetically by series
                    // all books without a series input will be moved to bottom of list
                    case 4:
                        Book.bookArrayList.sort(Comparator.comparing(Book::getSeries));
                        setBookAdapter();
                        break;
                    // if genre is selected organise list alphabetically by genre
                    case 5:
                        Book.bookArrayList.sort(Comparator.comparing(Book::getGenre));
                        setBookAdapter();
                        break;
                    // if pages is selected organise list numerically by pages
                    case 6:
                        Book.bookArrayList.sort(Comparator.comparing(Book::getPages));
                        setBookAdapter();
                        break;
                    // if status is selected organise list alphabetically by status
                    case 7:
                        Book.bookArrayList.sort(Comparator.comparing(Book::getStatus));
                        setBookAdapter();
                        break;
                    // if bookmark is selected organise list numerically by bookmark
                    case 8:
                        Book.bookArrayList.sort(Comparator.comparing(Book::getBookmark));
                        setBookAdapter();
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // catch Index out of bounds exception if there are not enough books in the list
                try {
                    // get current first and second book from list
                    Book book1 = Book.bookArrayList.get(0);
                    Book book2 = Book.bookArrayList.get(1);

                    // check users spinner selection
                    switch (position) {
                        case 0:
                            // sort books in ascending order
                            findSortType(sortSpinner, book2, book1);
                            break;
                        case 1:
                            // sort books in descending order
                            findSortType(sortSpinner, book1, book2);
                            break;
                    }
                } catch (IndexOutOfBoundsException ex) {
                    Toast.makeText(LibraryActivity.this, "Add some books", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checked = bookListView.getCheckedItemPositions();

                if (checked.size() == 0) {
                    Toast.makeText(LibraryActivity.this, "No books have been selected for removal", Toast.LENGTH_SHORT).show();
                    btnBin.setVisibility(View.GONE);
                    bookListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    return;
                }

                // find database for current application context
                SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(getApplicationContext());



                for (int i = 0; i < bookListView.getCount(); i++) {
                    if (checked.get(i)) {
                        Book book = (Book) bookListView.getItemAtPosition(i);

                        sqLiteManager.removeBookFromDatabase(book, getString(R.string.library_table));

                        Log.i("TAG", "onClick: selected " + book.getTitle());
                    }
                }

                // once selected item has been deleted hide bin icon
                btnBin.setVisibility(View.GONE);

                // restart activity
                finish();
                startActivity(getIntent());


            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // assign book to selected variable when selected in listview
                selected = Book.bookArrayList.get(position);


            }

        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else if (itemId == R.id.nav_wishlist) {
                    Intent intent = new Intent(LibraryActivity.this, WishlistActivity.class);
                    finish();
                    startActivity(intent);
                } else if (itemId == R.id.nav_profile) {
                    Toast.makeText(LibraryActivity.this, "Profile changes must be made in Home page", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });



    }

    private void displayName() {
        View headerView = navigationView.getHeaderView(0);
        EditText usernameEditText = headerView.findViewById(R.id.txt_nav_username);

        String contextUsername = getUsername(this);


        usernameEditText.setText(contextUsername);
    }


    private void loadFromDBToMemory() {
        // load device database
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateBookList();
    }


    private void initWidgets() {

        drawerLayout = findViewById(R.id.drawerLayout_library);
        navigationView = findViewById(R.id.navView_library);
        toolbar = findViewById(R.id.toolbar_library);
        setSupportActionBar(toolbar);

        btnAdd = findViewById(R.id.btn_library_add);
        btnEdit = findViewById(R.id.btn_library_edit);
        btnRemove = findViewById(R.id.btn_library_remove);

        sortSpinner = findViewById(R.id.spinner_lib_sort);
        orderSpinner = findViewById(R.id.spinner_lib_order);

        btnBin = findViewById(R.id.imagebtn_library_bin);

        bookCount = findViewById(R.id.txt_library_bookNum);

        bookListView = findViewById(R.id.bookListView);
    }

    private void setBookAdapter() {

        // initialise book adapter with books loaded from database
        BookAdapter bookAdapter = new BookAdapter(getApplicationContext(), Book.bookArrayList);
        bookListView.setAdapter(bookAdapter);
    }

    /*
    private Intent getBookToEdit(Intent i) {

        String editTitle = selected.getTitle();
        String editAuthor = selected.getAuthor();
        String editSeries = selected.getSeries();
        String editGenre = selected.getGenre();
        String editPages = selected.getPages();
        String editStatus = selected.getStatus();
        String editBookmark = selected.getBookmark();



        i.putExtra("Title", editTitle);
        i.putExtra("Author", editAuthor);
        i.putExtra("Series", editSeries);
        i.putExtra("Genre", editGenre);
        i.putExtra("Pages", editPages);
        i.putExtra("Status", editStatus);
        i.putExtra("Bookmark", editBookmark);

        return i;
    }*/

    private void findSortType(Spinner sort, Book book1, Book book2) {

        // get current sort option
        String sortOption = sort.getSelectedItem().toString();

        // reverse list based on current sort option
        switch (sortOption) {
            case "Title":
                if (book1.getTitle().compareTo(book2.getTitle()) < 0) {
                    reverseList();
                }
                break;
            case "Author":
                if (book1.getAuthor().compareTo(book2.getAuthor()) < 0) {
                    reverseList();
                }
                break;
            case "Series":
                if (book1.getSeries().compareTo(book2.getSeries()) < 0) {
                    reverseList();
                }
                break;
            case "Genre":
                if (book1.getGenre().compareTo(book2.getGenre()) < 0) {
                    reverseList();
                }
                break;
            case "Pages":
                if (book1.getPages().compareTo(book2.getPages()) < 0) {
                    reverseList();
                }
                break;
            case "Status":
                if (book1.getStatus().compareTo(book2.getStatus()) < 0) {
                    reverseList();
                }
                break;
            case "Bookmark":
                if (book1.getBookmark().compareTo(book2.getBookmark()) < 0) {
                    reverseList();
                }
                break;
            default:
                reverseList();
                break;
        }
    }

    public void goToFullDetails() {
        // initalise move to full detail activity
        Intent intent = new Intent(this, FullBookDetailsActivity.class);

        // attach selected book information to intent
        intent.putExtra("title", selected.getTitle());
        intent.putExtra("author", selected.getAuthor());

        startActivity(intent);
    }

    private void reverseList() {
        Collections.reverse(Book.bookArrayList);
        setBookAdapter();
    }


    @Override
    protected void onResume() {
        super.onResume();
        bookCount.setText(String.valueOf(Book.bookArrayList.size()));
        setBookAdapter();
    }

    private boolean returnFromRemoveSelection() {

        if (btnBin.isShown()) {
            btnBin.setVisibility(View.GONE);

            // return list to single selection mode
            bookListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // clear list view selected item/s
            bookListView.setItemChecked(-1, true);
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
            Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
