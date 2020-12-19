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


    public PoundInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.codeno);
        dest.writeString(this.truckno);
        dest.writeString(this.cargotype);
        dest.writeString(this.seller);
        dest.writeString(this.weighman);
        dest.writeString(this.shipper);
        dest.writeString(this.driver);
        dest.writeString(this.receiver);
        dest.writeDouble(this.allupweight);
        dest.writeDouble(this.truckweight);
        dest.writeDouble(this.percent);
        dest.writeDouble(this.cargoweight);
        dest.writeDouble(this.price);
        dest.writeDouble(this.total);
        dest.writeInt(this.status);
        dest.writeString(this.indate);
        dest.writeString(this.outdate);
        dest.writeString(this.remark);
        dest.writeInt(this.templetid);
        dest.writeInt(this.styleid);
        dest.writeString(this.url);
        dest.writeString(this.createDate);
        dest.writeString(this.modifyDate);
    }

    protected PoundInfo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.name = in.readString();
        this.code = in.readString();
        this.codeno = in.readString();
        this.truckno = in.readString();
        this.cargotype = in.readString();
        this.seller = in.readString();
        this.weighman = in.readString();
        this.shipper = in.readString();
        this.driver = in.readString();
        this.receiver = in.readString();
        this.allupweight = in.readDouble();
        this.truckweight = in.readDouble();
        this.percent = in.readDouble();
        this.cargoweight = in.readDouble();
        this.price = in.readDouble();
        this.total = in.readDouble();
        this.status = in.readInt();
        this.indate = in.readString();
        this.outdate = in.readString();
        this.remark = in.readString();
        this.templetid = in.readInt();
        this.styleid = in.readInt();
        this.url = in.readString();
        this.createDate = in.readString();
        this.modifyDate = in.readString();
    }

    public static final Creator<PoundInfo> CREATOR = new Creator<PoundInfo>() {
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
