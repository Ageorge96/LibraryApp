package com.example.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "LibraryDB";
    private static final int DATABASE_VERSION = 2;

    // tables
    private static final String TABLE_NAME_LIBRARY = "library";
    private static final String TABLE_NAME_WISHLIST = "wishlist";

    // table fields
    private static final String TITLE_FIELD = "title";
    private static final String AUTHOR_FIELD = "author";
    private static final String SERIES_FIELD = "series";
    private static final String GENRE_FIELD = "genre";
    private static final String PAGES_FIELD = "pages";
    private static final String STATUS_FIELD = "status";
    private static final String BOOKMARK_FIELD = "bookmark";
    private static final String COVER_FIELD = "cover";
    private static final String RATING_FIELD = "rating";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String PRICE_FIELD = "price";

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {

        // check if database exists
        if (sqLiteManager == null) {
            // create database
            sqLiteManager = new SQLiteManager(context);

        }


        return sqLiteManager;
    }


    // create tables for database when new database object is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME_LIBRARY)
                .append(" (")
                .append(TITLE_FIELD)
                .append(" TEXT NOT NULL, ")
                .append(AUTHOR_FIELD)
                .append(" TEXT NOT NULL, ")
                .append(SERIES_FIELD)
                .append(" TEXT DEFAULT \"N/A\", ")
                .append(GENRE_FIELD)
                .append(" TEXT NOT NULL, ")
                .append(PAGES_FIELD)
                .append(" INTEGER, ")
                .append(STATUS_FIELD)
                .append(" TEXT, ")
                .append(BOOKMARK_FIELD)
                .append(" INTEGER DEFAULT 0 NOT NULL, ")
                .append(COVER_FIELD)
                .append(" TEXT, ")
                .append(RATING_FIELD)
                .append(" TEXT, ")
                .append(DESCRIPTION_FIELD)
                .append(" TEXT, ")
                .append(PRICE_FIELD)
                .append(" TEXT);");

        StringBuilder sql2;
        sql2 = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME_WISHLIST)
                .append(" (")
                .append(TITLE_FIELD)
                .append(" TEXT NOT NULL, ")
                .append(AUTHOR_FIELD)
                .append(" TEXT NOT NULL, ")
                .append(SERIES_FIELD)
                .append(" TEXT DEFAULT \"N/A\", ")
                .append(GENRE_FIELD)
                .append(" TEXT NOT NULL, ")
                .append(PAGES_FIELD)
                .append(" INTEGER, ")
                .append(STATUS_FIELD)
                .append(" TEXT, ")
                .append(BOOKMARK_FIELD)
                .append(" INTEGER DEFAULT 0 NOT NULL, ")
                .append(COVER_FIELD)
                .append(" TEXT, ")
                .append(RATING_FIELD)
                .append(" TEXT, ")
                .append(DESCRIPTION_FIELD)
                .append(" TEXT, ")
                .append(PRICE_FIELD)
                .append(" TEXT);");

        db.execSQL(sql.toString());
        db.execSQL(sql2.toString());

        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    // input new books to library table
    public void addBookToLirary(Book book) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE_FIELD, book.getTitle());
        contentValues.put(AUTHOR_FIELD, book.getAuthor());
        contentValues.put(SERIES_FIELD, book.getSeries());
        contentValues.put(GENRE_FIELD, book.getGenre());
        contentValues.put(PAGES_FIELD, book.getPages());
        contentValues.put(STATUS_FIELD, book.getStatus());
        contentValues.put(BOOKMARK_FIELD, book.getBookmark());
        contentValues.put(COVER_FIELD, book.getCover());
        contentValues.put(RATING_FIELD, book.getRating());
        contentValues.put(DESCRIPTION_FIELD, book.getDescription());
        contentValues.put(PRICE_FIELD, book.getGooglePrice());

        sqLiteDatabase.insert(TABLE_NAME_LIBRARY, null, contentValues);

        //clear table
        //sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME_LIBRARY);

    }

    // input new books into wishlish table
    public void addBookToWishlist(Book book) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE_FIELD, book.getTitle());
        contentValues.put(AUTHOR_FIELD, book.getAuthor());
        contentValues.put(GENRE_FIELD, book.getGenre());
        contentValues.put(PAGES_FIELD, book.getPages());
        contentValues.put(COVER_FIELD, book.getCover());
        contentValues.put(RATING_FIELD, book.getRating());
        contentValues.put(DESCRIPTION_FIELD, book.getDescription());
        contentValues.put(PRICE_FIELD, book.getGooglePrice());

        sqLiteDatabase.insert(TABLE_NAME_WISHLIST, null, contentValues);

        //clear table
        //sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME_WISHLIST);
    }

    public void removeBookFromDatabase(Book book, String table) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(table, TITLE_FIELD + "=? AND " + AUTHOR_FIELD + "=?", new String[]{book.getTitle(), book.getAuthor()});

    }


    public void populateBookList() {

        // clear pre-existing list before intent refresh
        Book.bookArrayList.removeAll(Book.bookArrayList);

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // collect all data from library table
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_LIBRARY, null)) {
            if(result.getCount() != 0) {

                while (result.moveToNext()) {
                    String title = result.getString(0);
                    String author = result.getString(1);
                    String series = result.getString(2);
                    String genre = result.getString(3);
                    String pages = result.getString(4);
                    String status = result.getString(5);
                    String bookmark = result.getString(6);
                    String cover = result.getString(7);
                    String description = result.getString(9);


                    Book book = new Book(title, author, series, genre, pages, status, bookmark, cover, null, description, null);

                    Book.bookArrayList.add(book);

                }
            }

        }

    }

    public void populateWishlist() {

        Log.i("TAG", "populateWishlist: " + TABLE_NAME_WISHLIST);

        // clear all data from pre-existing wishlist
        Book.wishArrayList.removeAll(Book.wishArrayList);

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // collect all data from wishlist table
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_WISHLIST, null)) {
            if (result != null) {

                while (result.moveToNext()) {

                    String title = result.getString(0);
                    String author = result.getString(1);
                    String series = result.getString(2);
                    String genre = result.getString(3);
                    String pages = result.getString(4);
                    String status = result.getString(5);
                    String bookmark = result.getString(6);
                    String cover = result.getString(7);
                    String rating = result.getString(8);
                    String description = result.getString(9);
                    String price = result.getString(10);

                    Book book = new Book(title, author, series, genre, pages, status, bookmark, cover, rating, description, price);

                    Book.wishArrayList.add(book);
                }
            }


        }

    }

    // change book details with new user inputs
    public void updateBookDB(String oldTitle, String oldAuthor, Book updatedBook) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // supply values with new book details
        contentValues.put(TITLE_FIELD, updatedBook.getTitle());
        contentValues.put(AUTHOR_FIELD, updatedBook.getAuthor());
        contentValues.put(SERIES_FIELD, updatedBook.getSeries());
        contentValues.put(GENRE_FIELD, updatedBook.getGenre());
        contentValues.put(PAGES_FIELD, updatedBook.getPages());
        contentValues.put(STATUS_FIELD, updatedBook.getStatus());
        contentValues.put(BOOKMARK_FIELD, updatedBook.getBookmark());
        contentValues.put(COVER_FIELD, updatedBook.getCover());
        contentValues.put(RATING_FIELD, updatedBook.getRating());
        contentValues.put(DESCRIPTION_FIELD, updatedBook.getDescription());
        contentValues.put(PRICE_FIELD, updatedBook.getGooglePrice());

        // find book with original title and author values
        sqLiteDatabase.update(TABLE_NAME_LIBRARY, contentValues,TITLE_FIELD + "=? and " + AUTHOR_FIELD + "=?", new String[]{oldTitle, oldAuthor});
        // update book list with changed values
        populateBookList();

    }

    // get details of select book
    public Book getAllDetails(String getTitle, String getAuthor) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_LIBRARY
                + " WHERE " + TITLE_FIELD + " = ? AND " + AUTHOR_FIELD + " = ?",
                new String[] {getTitle, getAuthor});

        if (result.getCount() != 0) {
            result.moveToNext();

            String title = result.getString(0);
            String author = result.getString(1);
            String series = result.getString(2);
            String genre = result.getString(3);
            String pages = result.getString(4);
            String status = result.getString(5);
            String bookmark = result.getString(6);
            String cover = result.getString(7);
            String description = result.getString(9);

            Book selectedBook = new Book(title, author, series, genre, pages, status, bookmark, cover, null, description, null);

            return selectedBook;

        }

        return null;
    }


}
