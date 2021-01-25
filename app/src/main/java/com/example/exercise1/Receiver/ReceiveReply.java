package com.example.exercise1.Receiver;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.exercise1.Fragments.DownloadFragment;

public class ReceiveReply extends BroadcastReceiver {
    public static String KEY_TEXT_REPLY = "key_text_reply";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        String receiver = intent.getStringExtra("receiver");
        if(remoteInput != null){
            String message = (String) remoteInput.getCharSequence(KEY_TEXT_REPLY);
            sendmessage(message, receiver,context);
        }
    }
    public void sendmessage(String message, String receiver, Context ctx){
        Intent intent = new Intent(ctx, DownloadFragment.class);
        PendingIntent pi = PendingIntent.getActivity(ctx,0,intent,0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(receiver,null,message,pi,null);
    }
}
