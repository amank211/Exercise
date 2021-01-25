package com.example.exercise1.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.exercise1.DownloadActivity;
import com.example.exercise1.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService extends Service {
    String mImageName;
    String mUrl;
    private static final int REQUEST_CODE = 100;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUrl = intent.getStringExtra("url");
        mImageName = intent.getStringExtra("name");
        Intent resultintent = new Intent(getBaseContext(), DownloadActivity.class);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Looper.prepare();
                    resultintent.putExtra("path",downloadimage(mUrl));
                    resultintent.putExtra("name", mImageName);
                    resultintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(resultintent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public String downloadimage(String url){
        Bitmap bmp;
        File myFile =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            try {
                bmp = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
                String state = Environment.getExternalStorageState();
                File file = new File(myFile, mImageName + ".jpg");
                FileOutputStream os = null;
                if(Environment.MEDIA_MOUNTED.equals(state)){
                    if(Build.VERSION.SDK_INT >= 23){
                        if(checkpermission()){
                            try{
                                os = new FileOutputStream(file);
                                bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                os.flush();
                                os.close();
                            } catch(FileNotFoundException e){
                                e.printStackTrace();
                            } catch(IOException e){
                                e.printStackTrace();
                            }
                        } else{
                            requestPermions();
                            return null;
                        }
                    }
                }
            } catch (MalformedURLException e){}
            catch (IOException e){}
        return  myFile.toString() + "/" + mImageName + ".jpg";
    }
    private boolean checkpermission(){
        int result = ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }
    private void requestPermions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(new MainActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(getBaseContext(), "allow this permission", Toast.LENGTH_SHORT);
        } else{
            ActivityCompat.requestPermissions(new MainActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }

}
