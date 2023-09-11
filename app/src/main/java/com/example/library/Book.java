package com.example.library;

import android.util.Log;

import java.util.ArrayList;

public class Book {

    public static ArrayList<Book> bookArrayList = new ArrayList<>();
    public static ArrayList<Book> wishArrayList = new ArrayList<>();

    private String title;
    private String author;
    private String series;
    private String genre;
    private String pages;
    private String status;
    private String bookmark;


    private String cover;
    private String rating;
    private String description;
    private String googlePrice;

    public Book(String title, String author, String series, String genre, String pages, String status, String bookmark, String cover, String rating, String description, String price) {

        this.title = title;
        this.author = author;
        this.series = series;
        this.genre = genre;
        this.pages = pages;
        this.status = status;
        this.bookmark = bookmark;

        this.cover = cover;
        this.rating = rating;
        this.description = description;
        this.googlePrice = price;
    }


    //setters and getters grouping
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSeries() {

        if (series.equals("")) {
            series = "~N/A";
        }

        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPages() {

        if (pages.equals("")) {
            pages = "~No pages added";
        }

        return pages;
    }

    public void setPages(String pages) {


        this.pages = pages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookmark() {

        if (bookmark.equals("")) {
            bookmark = "~No bookmark added";
        } else if (bookmark.equals("0")) {
            bookmark = "~No bookmark added";
        }

        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getCover() {

        if (cover == null) {
            cover = "~No cover provided";
        }

        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRating() {

        if (rating == null) {
            rating = "~No rating provided";
        }

        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {

        if (description == null) {
            description = "~No description provided";
        }

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGooglePrice() {

        if (googlePrice == null || googlePrice.equals("")) {
            googlePrice = "~No price provided";
        } else if (googlePrice.equals("£")) {
            googlePrice = "~No price provided";
        } else if (!googlePrice.contains(".") && !googlePrice.equals("~No price provided")) {
            googlePrice = googlePrice + ".00";
        }

        return googlePrice;
    }

    public void setGooglePrice(String price) {
        this.googlePrice = price;
    }
}
