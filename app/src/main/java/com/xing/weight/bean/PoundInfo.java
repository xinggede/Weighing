package com.xing.weight.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * 磅单信息
 */
public class PoundInfo implements Parcelable {

    public int id;

    public String title;

    public String name;

    public String code;

    public String codeno;

    public String truckno;

    public String cargotype;

    public String seller;

    public String weighman;

    public String shipper;

    public String driver;

    public String receiver;

    public double allupweight;

    public double truckweight;

    public double percent;

    public double cargoweight;

    public double price;

    public double total;

    public int status;

    public String indate;

    public String outdate;

    public String remark;

    public int templetid;

    public int styleid;

    public String url;

    public String createDate;

    public String modifyDate;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
    }

    public PoundInfo() {
    }

    protected PoundInfo(Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<PoundInfo> CREATOR = new Parcelable.Creator<PoundInfo>() {
        @Override
        public PoundInfo createFromParcel(Parcel source) {
            return new PoundInfo(source);
        }

        @Override
        public PoundInfo[] newArray(int size) {
            return new PoundInfo[size];
        }
    };
}
