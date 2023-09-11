package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod
        .InputMethodManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.AccessibleObject;

public class Operations extends AppCompatActivity {

    public static String toTitleCase (String text) {

        // initialise new StringBuilder object to store new string
        StringBuilder titleCase = new StringBuilder((text.length()));
        // boolean to identify if array is at new word
        // string starts with new word, so boolean starts as true
        boolean nextTitle = true;

        for (char c : text.toCharArray()) {

            // check if character is a space
            if (Character.isSpaceChar(c)) {
                // if the character is a string we are looking for a new work
                // therefore set boolean to true
                nextTitle = true;


            }
            // check if we are looking for a new word and the character is a letter
            else if (nextTitle && Character.isAlphabetic(c)) {
                // set letter to uppercase
                c = Character.toUpperCase(c);
                // first character of word has been update
                // set boolean to false and wait for new word
                nextTitle = false;

            }
            // check if we are looking for a new word and if the character is a letter
            else if (!nextTitle && Character.isAlphabetic(c)) {
                // the character is no the first character of the string
                // set character to lower case
                c = Character.toLowerCase(c);
            }

            // add character to stringBuilder object
            titleCase.append(c);
        }

        // return title case string
        return titleCase.toString();
    }


    // confirm if input is an integer
    public static boolean isNumeric (String input) {
        try {
            // try to convert string input into an int
            Integer.parseInt(input);
        }
        // catch incorrect input format
        catch (NumberFormatException ex) {
            // the input is not an integer return false
            return false;
        }

        // the input is an integer return true
        return true;
    }


    public static boolean isCurrency(String value) {

        try {
            value = value.substring(1);
        } catch (StringIndexOutOfBoundsException e) {
            return true;
        }

        if (value.contains(".")) {

            String[] co = value.split("[.]");
            if (isNumeric(co[0]) && isNumeric(co[1])) return true;

        } else if (isNumeric(value)) {
            return true;
        }

        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
