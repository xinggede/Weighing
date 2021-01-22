package com.xing.weight.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * 磅单模板
 */
public class PoundItemInfo implements Parcelable {

    public PoundType type; //字段类型

    public int inputType = 0; // 0:文本  1:数字  2：电话  3:多行输入

    public int lenght = 20; //输入内容长度限制

    public String name; //名称

    public String value; //输入的内容

    public String hint; //提示信息

    public boolean isChecked = false;

    public PoundItemInfo(String name) {
        this.name = name;
    }


    public static enum PoundType {
        /**
         * 编号
         */
        CODE(0),
        CNAME(1), //公司名
        CBOSS(2), //负责人
        CPHONE(3), //电话
        CADDRESS(4), //地址
        GTYPE(5), //货物类型
        CARWEIGHT(6), //车重
        INTIME(7), //入场时间
        OUTTIME(8), //出场时间 | 录单日期
        TOTALWEIGHT(9), //总重
        REALWEIGHT(10), //净重
        DISCOUNT(11), //折扣
        PRICE(12), //单价
        TOTALPRICE(13), //总价
        PERSON(14), //司磅员
        RECEIVENAME(15), //接收方
        DRIVERCODE(16), //车牌
        DRIVER(17), //司机
        REMARKS(18), //备注
        MODELNAME(19),//模板名称
        MODELSTYLE(20),//模板样式
        JSR(21),//经手人
        ORDERNUMBER(22),//订单号
        SERIALNUMBER(23),//序号
        RECEIVECONTACTS(24), //接收方联系人
        RECEIVEPHONE(25), //接收方电话
        RECEIVEADDRESS(26), //接收方地址
        QSR(27), //签收
        SHFS(28), //送货方式


        ADD(100); //按钮

        private final int value;

        private PoundType(int value) {
            this.value = value;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.inputType);
        dest.writeInt(this.lenght);
        dest.writeString(this.name);
        dest.writeString(this.value);
        dest.writeString(this.hint);
    }

    protected PoundItemInfo(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : PoundType.values()[tmpType];
        this.inputType = in.readInt();
        this.lenght = in.readInt();
        this.name = in.readString();
        this.value = in.readString();
        this.hint = in.readString();
    }

    public static final Parcelable.Creator<PoundItemInfo> CREATOR = new Parcelable.Creator<PoundItemInfo>() {
        @Override
        public PoundItemInfo createFromParcel(Parcel source) {
            return new PoundItemInfo(source);
        }

        @Override
        public PoundItemInfo[] newArray(int size) {
            return new PoundItemInfo[size];
        }
    };
}
