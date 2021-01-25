package com.example.exercise1.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.exercise1.Fragments.DownloadFragment;
import com.example.exercise1.R;

public class MessageService extends Service {
    static String KEY_TEXT_REPLY = "key_text_reply";

    StatusBarNotification[] mNotifications;
    NotificationManager mNotificationManager;
    String mChannelId = "download_1234";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        String receiver = intent.getStringExtra("receiver");
        int id = intent.getIntExtra("ntfid", 0);
        mNotificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if(remoteInput != null){
            String message = (String) remoteInput.getCharSequence(KEY_TEXT_REPLY);
            Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
            //sendmessage(message, receiver,getApplication());
            @SuppressLint({"NewApi", "LocalSuppress"}) Notification notification =
                    new Notification.Builder(getApplicationContext())
                    .setContentTitle("Message")
                    .setContentText("Replied")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setChannelId(mChannelId)
                    .build();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startForeground(1001, notification);

            //mNotificationManager.notify(id,notification);
        }
        return START_STICKY;
    }
    public void sendmessage(String message, String receiver, Context ctx){
        Intent intent = new Intent(ctx, DownloadFragment.class);
        PendingIntent pi = PendingIntent.getActivity(ctx,0,intent,0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(receiver,null,message,pi,null);
    }
}