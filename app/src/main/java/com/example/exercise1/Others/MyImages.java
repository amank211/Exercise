package com.example.exercise1.Others;

import android.os.Parcelable;

import java.io.Serializable;

public class MyImages implements Serializable {
    String name;
    String type;
    String url;

    public MyImages(String name, String url ) {
        this.name=name;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String geturl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

}
