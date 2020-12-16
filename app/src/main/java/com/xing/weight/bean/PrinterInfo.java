package com.xing.weight.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class PrinterInfo implements Parcelable {

    public int id;
    public String name;
    public String devcode;
    public String verfycode;
    public String norms;
    public String remark;
    public String createDate;
    public String modifyDate;

    @Override
    public String toString() {
        return name+":"+ devcode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.devcode);
        dest.writeString(this.verfycode);
        dest.writeString(this.norms);
        dest.writeString(this.remark);
        dest.writeString(this.createDate);
        dest.writeString(this.modifyDate);
    }

    public PrinterInfo() {
    }

    protected PrinterInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.devcode = in.readString();
        this.verfycode = in.readString();
        this.norms = in.readString();
        this.remark = in.readString();
        this.createDate = in.readString();
        this.modifyDate = in.readString();
    }

    public static final Parcelable.Creator<PrinterInfo> CREATOR = new Parcelable.Creator<PrinterInfo>() {
        @Override
        public PrinterInfo createFromParcel(Parcel source) {
            return new PrinterInfo(source);
        }

        @Override
        public PrinterInfo[] newArray(int size) {
            return new PrinterInfo[size];
        }
    };
}
