package com.example.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.exercise1.R;

import java.io.File;

public class DownloadActivity extends AppCompatActivity {
    Bitmap mBitmap;
    File mImgFile;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Intent resultintent = getIntent();
        String name = resultintent.getStringExtra("name");
        mImgFile = new File(resultintent.getStringExtra("path"));
        if(mImgFile.exists()){
            mBitmap = BitmapFactory.decodeFile(mImgFile.getAbsolutePath());
            mImage = (ImageView) findViewById(R.id.downloaded);
            setTitle(resultintent.getStringExtra("name"));
            mImage.setImageBitmap(mBitmap);
        }
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
    }
}