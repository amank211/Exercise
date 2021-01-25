package com.example.exercise1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.exercise1.Others.MyImages;
import com.example.exercise1.R;

import java.util.List;
//Adapter class for Recyclerview
public class RecyclerAdater extends RecyclerView.Adapter<RecyclerAdater.MyViewholder> {

    Context mContext;
    List<MyImages> mImages;
    OnQuantityChangeListener mListener;
    int mSelecteditem = -1;

    public RecyclerAdater(Context context, List<MyImages> myImages, OnQuantityChangeListener listener){
        this.mImages = myImages;
        this.mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        holder.mName.setText(mImages.get(position).getName());
        Glide.with(holder.mImageView.getContext())
                .load(mImages.get(position).geturl())
                .centerCrop().
                into(holder.mImageView);
        holder.mSelection.setChecked(position == mSelecteditem);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public String getSelectedName(){
        return mImages.get(mSelecteditem).getName();
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange( int selected, String url );
    }
    public class MyViewholder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mName;
        RadioButton mSelection;
        public MyViewholder(View itemview){
            super(itemview);
            mImageView = itemview.findViewById(R.id.myimage);
            mName = itemview.findViewById(R.id.name);
            mSelection = itemview.findViewById(R.id.selection);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelecteditem = getAdapterPosition();
                    mListener.onQuantityChange( mSelecteditem, mImages.get(mSelecteditem).geturl());
                    notifyDataSetChanged();
                }
            };
            itemview.setOnClickListener(clickListener);
            mSelection.setOnClickListener(clickListener);
        }
    }
}