package com.example.exercise1.Others;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import java.io.Serializable;

public class MyNotificationManager implements Serializable {
    int mContext;
    Notification.Builder builder;

    public MyNotificationManager(int context){
        this.mContext = context;
    }


}
