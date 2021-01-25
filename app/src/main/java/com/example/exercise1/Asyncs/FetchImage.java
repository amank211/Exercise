package com.example.exercise1.Asyncs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
//Asynctask for fetching image from given url
//returns Bitmap
public class FetchImage extends AsyncTask<String, Integer, Bitmap> {
    URL mImageUrl;
    HttpURLConnection mConn;
    Bitmap mBmp;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            mImageUrl = new URL(urls[0]);
            mConn = (HttpURLConnection)mImageUrl.openConnection();
            mBmp = BitmapFactory.decodeStream(mConn.getInputStream());
            return mBmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
    }
}
