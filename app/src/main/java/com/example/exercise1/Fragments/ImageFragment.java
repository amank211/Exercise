package com.example.exercise1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise1.Others.MyImages;
import com.example.exercise1.Adapters.RecyclerAdater;
import com.example.exercise1.R;

import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {

    onItemSelectListener mListener;
    List<MyImages> mImages = new ArrayList<>();
    List<MyImages> mPopulateImages = new ArrayList<>();
    String mUrl;
    int count = 0;

    RecyclerView mRecyclerView;
    RecyclerAdater mAdapter;

    public interface onItemSelectListener{
        void onItemclicked(int selected, String url);
    }
    boolean changedata(){
        if(count<10){
            count++;
            mPopulateImages.addAll(mImages.subList(((count -1) *10),(count) *10));
            return true;
        } else return false;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_fragment, container, false);

        mImages = (List<MyImages>) getArguments().getSerializable("data");
        changedata();
        mRecyclerView = v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new RecyclerAdater(getActivity(),
                mPopulateImages, new RecyclerAdater.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange(int selectedit, String url) {
                mUrl = url;
                Log.d("onQuantity: ", url);
                mListener.onItemclicked(selectedit,url);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if(changedata()){
                        mAdapter.notifyItemRangeInserted((count-1) *10, mPopulateImages
                                .size());
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public void setdata(int value){
        mListener.onItemclicked(value, "fgds");
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onItemSelectListener) {
            mListener = (onItemSelectListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
