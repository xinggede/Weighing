package com.xing.weight.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.qmuiteam.qmui.arch.effect.Effect;

public class CustomInfo extends Effect implements Parcelable {

    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public CustomInfo() {
    }

    protected CustomInfo(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<CustomInfo> CREATOR = new Parcelable.Creator<CustomInfo>() {
        @Override
        public CustomInfo createFromParcel(Parcel source) {
            return new CustomInfo(source);
        }

        @Override
        public CustomInfo[] newArray(int size) {
            return new CustomInfo[size];
        }
    };
}
