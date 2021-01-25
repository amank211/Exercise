package com.example.exercise1.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.exercise1.Fragments.DownloadFragment;
import com.example.exercise1.Fragments.Frag1;
import com.example.exercise1.Fragments.Frag2;
import com.example.exercise1.Fragments.ImageFragment;
import com.example.exercise1.Fragments.fullImageFragment;
import com.example.exercise1.Others.MyImages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//Apapter class for viewpager
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    int mTotalTabs;
    List<MyImages> mImages;

    public MyViewPagerAdapter(Context c, FragmentManager fm, int totalTabs, List<MyImages> myImages){
        super(fm);
        mContext = c;
        this.mTotalTabs = totalTabs;
        this.mImages = myImages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ImageFragment fragment1 = new ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) mImages);
                fragment1.setArguments(bundle);
                return fragment1;
            case 1:
                return new fullImageFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}
