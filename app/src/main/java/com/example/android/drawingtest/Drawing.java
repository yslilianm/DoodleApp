package com.example.android.drawingtest;

import java.io.Serializable;

/**
 * Created by yslilianm on 2018/4/20.
 */

public class Drawing implements Serializable {
    public String title;
    public String bitmap;
    public String timestamp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Drawing(String title, String bitmap, String timestamp) {
        this.title = title;
        this.bitmap = bitmap;
        this.timestamp = timestamp;

    }

}


