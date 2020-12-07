package com.xing.weight.bean;

import com.qmuiteam.qmui.arch.effect.Effect;
import com.xing.weight.util.Tools;

public class GoodsDetail extends Effect {

    public int id;

    public String name;

    public String code;

    public String model;

    public String type;

    public String pricebuy;

    public double price;

    public String wastage;

    public double remark;

    public String createDate;

    public String modifyDate;

    public double quantity;

    public double amount;

    public String getCalcAmount() {
        return Tools.calcAmount(String.valueOf(quantity), String.valueOf(price));
    }
}
