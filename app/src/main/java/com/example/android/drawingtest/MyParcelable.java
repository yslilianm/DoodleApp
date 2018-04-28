package com.example.android.drawingtest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yslilianm on 2018/4/27.
 */

public class MyParcelable implements Parcelable {

    public static final MyParcelable.Creator<MyParcelable> CREATOR
            = new MyParcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };
    private int mData;

    /**
     * recreate object from parcel
     */
    private MyParcelable(Parcel in) {
        mData = in.readInt();


    }

    public int describeContents() {
        return 0;
    }

    /**
     * save object in parcel
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }
}