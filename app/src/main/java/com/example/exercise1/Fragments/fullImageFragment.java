package com.example.exercise1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.exercise1.MainActivity;
import com.example.exercise1.R;
import com.example.exercise1.Services.MediaService;

public class fullImageFragment extends Fragment {
    ImageView mImageView;
    TextView mTextView;
    Context mContext;
    String imagename;
    String myUrl;
    String mChannelId = "download_1234";
    Button mStart;

    public fullImageFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_image_fragment,container, false);
        mImageView = view.findViewById(R.id.selectedimage);
        mTextView = view.findViewById(R.id.selected);
        mStart = view.findViewById(R.id.for_service);
        ((MainActivity) getActivity()).updatelistener(new MainActivity.FragemntWire() {
            @Override
            public void passchangedselection(int selection, String url, String name) {
                myUrl = url;
                imagename = name;
                if(url!=null){
                    mTextView.setText(String.valueOf(selection));
                    Glide.with(mImageView.getContext()).load(url).centerCrop().into(mImageView);
                }
            }
        });



        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    isRunning = stopthisService();
                } else{
                    isRunning = startService();
                }

            }
        });

        return view;
    }
    boolean isRunning = false;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    boolean startService(){
        Intent serviceIntent = new Intent(getContext(), MediaService.class);
        serviceIntent.putExtra("channel_id", mChannelId);
        ContextCompat.startForegroundService(getContext(), serviceIntent);
        return true;
    }

    boolean stopthisService(){
        return false;
    }
}