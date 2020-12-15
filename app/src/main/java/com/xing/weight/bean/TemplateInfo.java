package com.xing.weight.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TemplateInfo implements Parcelable {

    public int id;
    public int type;
    public int styleid;
    public String name;
    public String stylename;
    public String content;
    public List<PoundItemInfo> contList;
    public String remark;
    public String createDate;
    public String modifyDate;


    public TemplateInfo() {
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
        dest.writeInt(this.type);
        dest.writeInt(this.styleid);
        dest.writeString(this.name);
        dest.writeString(this.stylename);
        dest.writeString(this.content);
        dest.writeTypedList(this.contList);
        dest.writeString(this.remark);
        dest.writeString(this.createDate);
        dest.writeString(this.modifyDate);
    }

    protected TemplateInfo(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.styleid = in.readInt();
        this.name = in.readString();
        this.stylename = in.readString();
        this.content = in.readString();
        this.contList = in.createTypedArrayList(PoundItemInfo.CREATOR);
        this.remark = in.readString();
        this.createDate = in.readString();
        this.modifyDate = in.readString();
    }

    public static final Creator<TemplateInfo> CREATOR = new Creator<TemplateInfo>() {
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
