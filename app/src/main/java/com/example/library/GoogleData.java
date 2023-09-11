package com.example.library;

import java.util.ArrayList;

public class GoogleData {

    public static ArrayList<GoogleData> googleDataArrayList = new ArrayList<>();

    public static GoogleData searchedBook;

    private String googleTitle;
    private String googleAuthor;
    private String pageCount;
    private String cover;
    private String rating;
    private String description;
    private String googlePrice;

    public GoogleData(
            String title,
            String author,
            String pageCount,
            String cover,
            String rating,
            String description,
            String price) {
        this.googleTitle = title;
        this.googleAuthor = author;
        this.pageCount = pageCount;
        this.cover = cover;
        this.rating = rating;
        this.description = description;
        this.googlePrice = price;
    }

    public String getGoogleTitle() {
        return googleTitle;
    }

    public void setGoogleTitle(String googleTitle) {
        this.googleTitle = googleTitle;
    }
    public String getGoogleAuthor() {
        return googleAuthor;
    }

    public void setGoogleAuthor(String googleAuthor) {
        this.googleAuthor = googleAuthor;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGooglePrice() {
        return googlePrice;
    }

    public void setGoogleDataArrayList(String price) {
        this.googlePrice = price;
    }

}
