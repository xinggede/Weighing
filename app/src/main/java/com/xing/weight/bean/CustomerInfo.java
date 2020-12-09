package com.xing.weight.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class CustomerInfo implements Parcelable {

    public int id;
    public String name;
    public String phone;
    public String address;
    public String remark;
    public int openid;
    public int comid;
    public String createDate;
    public String modifyDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.remark);
        dest.writeInt(this.openid);
        dest.writeInt(this.comid);
        dest.writeString(this.createDate);
        dest.writeString(this.modifyDate);
    }

    public CustomerInfo() {
    }

    protected CustomerInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.remark = in.readString();
        this.openid = in.readInt();
        this.comid = in.readInt();
        this.createDate = in.readString();
        this.modifyDate = in.readString();
    }

    public static final Parcelable.Creator<CustomerInfo> CREATOR = new Parcelable.Creator<CustomerInfo>() {
        @Override
        public CustomerInfo createFromParcel(Parcel source) {
            return new CustomerInfo(source);
        }

        @Override
        public CustomerInfo[] newArray(int size) {
            return new CustomerInfo[size];
        }
    };
}
