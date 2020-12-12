package com.xing.weight.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TemplateInfo implements Parcelable {

    public int id;
    public int type;
    public String name;
    public List<PoundItemInfo> content;
    public String remark;
    public String createDate;
    public String modifyDate;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeTypedList(this.content);
        dest.writeString(this.remark);
        dest.writeString(this.createDate);
        dest.writeString(this.modifyDate);
    }

    public TemplateInfo() {
    }

    protected TemplateInfo(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.name = in.readString();
        this.content = in.createTypedArrayList(PoundItemInfo.CREATOR);
        this.remark = in.readString();
        this.createDate = in.readString();
        this.modifyDate = in.readString();
    }

    public static final Parcelable.Creator<TemplateInfo> CREATOR = new Parcelable.Creator<TemplateInfo>() {
        @Override
        public TemplateInfo createFromParcel(Parcel source) {
            return new TemplateInfo(source);
        }

        @Override
        public TemplateInfo[] newArray(int size) {
            return new TemplateInfo[size];
        }
    };
}
