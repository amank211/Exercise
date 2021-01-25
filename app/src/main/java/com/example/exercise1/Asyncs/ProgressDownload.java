package com.example.exercise1.Asyncs;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.exercise1.MainActivity;
import com.example.exercise1.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
//asynctask for downloading image for given url
//shows the progress of download
public class ProgressDownload extends AsyncTask<String, Integer, Bitmap> {
    Context mContext;
    NotificationManager mNotificationManager;
    Notification.Builder mBuilder;
    String mImageName;
    String mChannelId;
    int mPer = 0;
    private static final int REQUEST_CODE = 100;

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;
        HttpURLConnection connection;
        InputStream is;
        ByteArrayOutputStream out;
        String state = Environment.getExternalStorageState();
        File myFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(myFile, mImageName + ".jpg");
        FileOutputStream os;
        if(Environment.MEDIA_MOUNTED.equals(state)){
            if(Build.VERSION.SDK_INT >= 23){
                if(checkpermission()){
                    try{
                        os = new FileOutputStream(file);
                        URL mImageUrl = new URL(strings[0]);
                        connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                        connection.connect();
                        final int length = connection.getContentLength();
                        is = new BufferedInputStream(connection.getInputStream(), length);
                        out = new ByteArrayOutputStream();
                        byte bytes[] = new byte[length];
                        int count;
                        long read = 0;
                        while ((count = is.read(bytes)) != -1) {
                            read += count;
                            out.write(bytes, 0, count);
                            publishProgress((int) ((read * 100) / length));
                        }
                        Log.d("size", String.valueOf(out.size()));
                        bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0,
                                out.size());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else{
                    requestPermions();
                    return null;
                }
            }
        return bitmap;
    }


    public ProgressDownload(Context context, String name, String channelId){
        this.mContext = context;
        this.mImageName = name;
        this.mChannelId = channelId;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mNotificationManager.cancel(0);
        mBuilder.setContentTitle("Download Complete");
        mBuilder.setContentText("");
        mBuilder.setProgress(0,0,false);
        mBuilder.setLargeIcon(bitmap);
        Log.d("Final value", String.valueOf(mPer));
        mNotificationManager.notify(0,mBuilder.build());
        super.onPostExecute(bitmap);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mPer = values[0];
        if((mPer % 2) == 0 && mPer<=90){
            mBuilder.setProgress(100,values[0], false);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(mContext)
                .setContentTitle("Downloading")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Download in Progress")
                .setChannelId(mChannelId);
    }

    private boolean checkpermission(){
        int result = ContextCompat.checkSelfPermission(mContext,
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
            Toast.makeText(mContext, "allow this permission", Toast.LENGTH_SHORT);
        } else{
            ActivityCompat.requestPermissions(new MainActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }
}
