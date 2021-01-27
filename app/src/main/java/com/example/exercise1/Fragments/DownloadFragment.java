package com.example.exercise1.Fragments;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.exercise1.DownloadActivity;
import com.example.exercise1.MainActivity;
import com.example.exercise1.Services.DownloadService;
import com.example.exercise1.Asyncs.FetchImage;
import com.example.exercise1.Services.MediaService;
import com.example.exercise1.Services.MessageService;
import com.example.exercise1.Others.MyNotificationManager;
import com.example.exercise1.Asyncs.ProgressDownload;
import com.example.exercise1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DownloadFragment extends Fragment {

    final int NOTIFY_ID = 1;
    final int REQUEST_CODE = 100;
    final String KEY_TEXT_REPLY = "key_text_reply";
    final String RECEIVER = "8888888888";

    FloatingActionButton mDownload;
    FloatingActionButton mMessage;
    FloatingActionButton mDownloadWithService;
    FloatingActionButton mProgressDownload;

    int mSelectedImage;
    String mUrl;
    String mImageName;
    String mChannelId = "download_1234";
    CharSequence mChannelName = "Downloads";
    String mMessageChannelId = "message_1234";
    CharSequence mMessageChannelName = "Chat";

    NotificationManager mNotificationManager;
    int mImportance = NotificationManager.IMPORTANCE_LOW;
    NotificationChannel mNotificationChannel =
            new NotificationChannel(mChannelId,mChannelName, mImportance);
    NotificationChannel mNotificationChannel2 =
            new NotificationChannel(mMessageChannelId,mMessageChannelName,mImportance);

    Notification.Builder mBuilder2;
    Bitmap bmp;

    public DownloadFragment(NotificationManager notificationManager){
        this.mNotificationManager = notificationManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.download_fragment,container, false);
        initalize(view);
        createchannel(mNotificationChannel, mChannelName, Color.GREEN);
        createchannel(mNotificationChannel2, mMessageChannelName, Color.BLUE);
        ((MainActivity) getActivity()).updatedownloadlistener(new MainActivity.FragemntWire() {
            @Override
            public void passchangedselection(int selection, String url, String name) {
                mSelectedImage = selection;
                mImageName = name;
                mUrl = url;
            }
        });

        mProgressDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUrl != null){
                    new ProgressDownload(getContext(),mImageName, mChannelId).execute(mUrl);
                } else {
                    Toast.makeText(getContext(), "Please, Select One", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });


        mDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                String me = downloadimage();
                if(me != null){
                    Intent result = new Intent(getContext(), DownloadActivity.class);
                    result.putExtra("path", me);
                    result.putExtra("name", mImageName);
                    PendingIntent pendingIntent =
                            PendingIntent.getActivity(getContext(),
                                    0, result, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new Notification.Builder(getContext())
                            .setContentTitle("Downloaded")
                            .setContentText(me)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setLargeIcon(bmp)
                            .setChannelId(mChannelId)
                            .setContentIntent(pendingIntent)
                            .build();
                    mNotificationManager.notify(NOTIFY_ID,notification);
                }
            }
        });
        mBuilder2 = new Notification.Builder(getContext())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Chats")
                .setContentText("messages")
                .setChannelId(mMessageChannelId);
        mMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                String message = downloadimage();
                if(message != null){
                    shownotification(message, mBuilder2, RECEIVER);

                }
            }
        });
        mDownloadWithService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUrl != null){
                    Intent serviceintent = new Intent(getContext(),DownloadService.class);
                    serviceintent.putExtra("url", mUrl);
                    serviceintent.putExtra("name", mImageName);
                    getActivity().startService(serviceintent);
                } else{
                    Toast.makeText(getContext(),
                            "Please, Select one to download",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    boolean isexist(StatusBarNotification[] notifications, int id){
        for(int i = 0; i< notifications.length; i++){
            if(notifications[i].getId() == id){
                return true;
            };
        }
        return false;
    }

    void initalize(View view){
        mDownload = view.findViewById(R.id.download);
        mMessage = view.findViewById(R.id.message);
        mDownloadWithService = view.findViewById(R.id.servicedownload);
        mProgressDownload = view.findViewById(R.id.progress_download);
    }

    void createchannel(NotificationChannel channel, CharSequence channelname, int c){
        channel.enableLights(true);
        channel.setLightColor(c);
        channel.enableVibration(true);
        channel.setName(channelname);
        mNotificationManager.createNotificationChannel(channel);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    void shownotification(String message, Notification.Builder builder2, String receiver){
        sendmessage(message, receiver);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Reply").build();
        Intent intent = new Intent(getContext(), MessageService.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("receiver", receiver);
        intent.putExtra("ntfid",NOTIFY_ID);
        MyNotificationManager myNotificationManager = new MyNotificationManager(0);
        Bundle bundle = new Bundle();
        bundle.putSerializable("builder", myNotificationManager);
        intent.putExtras(bundle);

        PendingIntent pendingIntent =
                PendingIntent.getService(getContext(),
                        0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Icon icon = Icon.createWithResource(getContext(), android.R.drawable.ic_dialog_info);
        StatusBarNotification[] allnotifications = mNotificationManager.getActiveNotifications();
        Notification.InboxStyle inboxStyle = (Notification.InboxStyle) builder2.getStyle();
        Notification.Action replyaction =
                                new Notification.Action.Builder(icon, "Reply",pendingIntent)
                                .addRemoteInput(remoteInput).build();
        if(isexist(allnotifications,NOTIFY_ID)){
            inboxStyle.addLine(mImageName + "message sent");
            Log.d("if:","true");
        } else{
            inboxStyle = new Notification.InboxStyle();
            inboxStyle.setBigContentTitle("Messages");
            builder2.setStyle(inboxStyle);
            inboxStyle.addLine(mImageName + "message sent");
            builder2.addAction(replyaction);
            Log.d("if:","false");
        }
        mNotificationManager.notify(NOTIFY_ID,builder2.build());
    }
    public String downloadimage(){
        if(mUrl != null){
            String state = Environment.getExternalStorageState();
            File myFile =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(myFile, mImageName + ".jpg");
            FileOutputStream os = null;
            if(Environment.MEDIA_MOUNTED.equals(state)){
                if(Build.VERSION.SDK_INT >= 23){
                    if(checkpermission()){
                        try{
                            os = new FileOutputStream(file);
                            FetchImage temp = (FetchImage) new FetchImage().execute(mUrl);
                            bmp = temp.get();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();
                        } catch(FileNotFoundException e){
                            e.printStackTrace();
                        } catch(IOException e){
                            e.printStackTrace();
                        } catch (ExecutionException e){
                            e.printStackTrace();
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), myFile.toString(), Toast.LENGTH_LONG).show();
                    } else{
                        requestPermisions();
                        return null;
                    }
                }
            }
            return  myFile.toString() + "/" + mImageName + ".jpg";
        } else{
            Toast.makeText(getContext(),"Please, Select one to download", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
    }

    public void sendmessage(String message, String receiver){
        Intent intent = new Intent(getContext(),DownloadFragment.class);
        PendingIntent pi = PendingIntent.getActivity(getContext(),0,intent,0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(receiver,null,message,pi,null);
        Toast.makeText(getContext(), "Message sent to " + receiver, Toast.LENGTH_SHORT);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e("value", "Granted");
                } else{
                    Log.e("value", "Denied");
                }
                break;
        }
    }
    private boolean checkpermission(){
        int result = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }
    private void requestPermisions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(getContext(), "allow this permission", Toast.LENGTH_SHORT);
        } else{
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }
}
