package com.example.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class DownloadURLImage extends AsyncTask<String, Void, Bitmap> {

    ImageView BmCover;

    public DownloadURLImage(ImageView BmCover) {
        this.BmCover = BmCover;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String  url = urls[0];
        Bitmap cover = null;

        try {
            InputStream in = new java.net.URL(url).openStream();
            cover = BitmapFactory.decodeStream(in);
        } catch (IOException | NetworkOnMainThreadException e) {
            Log.e("Error", "doInBackground: " + e);
            e.printStackTrace();
        }
        return cover;
    }

    protected void onPostExecute(Bitmap result) {
        BmCover.setImageBitmap(result);
    }

}
