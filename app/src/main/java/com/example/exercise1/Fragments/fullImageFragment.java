package com.example.exercise1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.exercise1.MainActivity;
import com.example.exercise1.R;

public class fullImageFragment extends Fragment {
    ImageView mImageView;
    TextView mTextView;
    Context mContext;
    String imagename;
    String myUrl;

    public fullImageFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_image_fragment,container, false);
        mImageView = view.findViewById(R.id.selectedimage);
        mTextView = view.findViewById(R.id.selected);
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
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}