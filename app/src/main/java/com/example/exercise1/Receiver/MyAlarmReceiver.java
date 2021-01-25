package com.example.exercise1.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.exercise1.R;

public class MyAlarmReceiver extends BroadcastReceiver {

    final String MESSAGE = "There are tons of images to download in my app";

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("8888888888",
                null, MESSAGE, null, null);
        Toast.makeText(context, "Your message has been sent. open messages to view it", Toast.LENGTH_LONG).show();
    }
}