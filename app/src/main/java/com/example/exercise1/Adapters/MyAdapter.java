package com.example.exercise1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.exercise1.Others.MyImages;
import com.example.exercise1.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


//Adapter class for listview
public class MyAdapter extends ArrayAdapter<MyImages> {
    List<MyImages> mImages;
    Context mContext;


    public MyAdapter(Context context, List<MyImages> myImages) {
        super(context, -1, myImages);
        this.mContext = context;
        this.mImages = myImages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        TextView url = (TextView) rowView.findViewById(R.id.url);
        ImageView image = (ImageView) rowView.findViewById(R.id.myimage);
        textView.setText(mImages.get(position).getName());
        url.setText(mImages.get(position).geturl());
        Glide.with(getContext())
                .load(mImages.get(position).geturl())
                .centerCrop()
                .into(image);
        return rowView;
    }


}
