package com.xing.weight.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AddPoundResultInfo implements Parcelable {

    public List<PrinterInfo> printList;

    public PoundInfo order;


    public AddPoundResultInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.printList);
        dest.writeParcelable(this.order, flags);
    }

    protected AddPoundResultInfo(Parcel in) {
        this.printList = in.createTypedArrayList(PrinterInfo.CREATOR);
        this.order = in.readParcelable(PoundInfo.class.getClassLoader());
    }

    public static final Creator<AddPoundResultInfo> CREATOR = new Creator<AddPoundResultInfo>() {
        @Override
        public AddPoundResultInfo createFromParcel(Parcel source) {
            return new AddPoundResultInfo(source);
        }

        @Override
        public AddPoundResultInfo[] newArray(int size) {
            return new AddPoundResultInfo[size];
        }
    };
}
