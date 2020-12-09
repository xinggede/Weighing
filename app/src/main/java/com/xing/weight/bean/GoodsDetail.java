package com.xing.weight.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.qmuiteam.qmui.arch.effect.Effect;
import com.xing.weight.util.Tools;

public class GoodsDetail extends Effect implements Parcelable {

    public int id;

    public String name;

    public String code;

    public String model;

    public String type;

    public String pricebuy;

    public double price;

    public String wastage;

    public String remark;

    public String createDate;

    public String modifyDate;

    public int openid;

    public int comid;

    public double quantity;

    public double amount;

    public String getCalcAmount() {
        return Tools.calcAmount(String.valueOf(quantity), String.valueOf(price));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.model);
        dest.writeString(this.type);
        dest.writeString(this.pricebuy);
        dest.writeDouble(this.price);
        dest.writeString(this.wastage);
        dest.writeString(this.remark);
        dest.writeString(this.createDate);
        dest.writeString(this.modifyDate);
        dest.writeDouble(this.quantity);
        dest.writeDouble(this.amount);
    }

    public GoodsDetail() {
    }

    protected GoodsDetail(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.code = in.readString();
        this.model = in.readString();
        this.type = in.readString();
        this.pricebuy = in.readString();
        this.price = in.readDouble();
        this.wastage = in.readString();
        this.remark = in.readString();
        this.createDate = in.readString();
        this.modifyDate = in.readString();
        this.quantity = in.readDouble();
        this.amount = in.readDouble();
    }

    public static final Parcelable.Creator<GoodsDetail> CREATOR = new Parcelable.Creator<GoodsDetail>() {
        @Override
        public GoodsDetail createFromParcel(Parcel source) {
            return new GoodsDetail(source);
        }

        @Override
        public GoodsDetail[] newArray(int size) {
            return new GoodsDetail[size];
        }
    };
}
