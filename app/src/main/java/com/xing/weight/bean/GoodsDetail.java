package com.xing.weight.bean;

import com.qmuiteam.qmui.arch.effect.Effect;
import com.xing.weight.util.Tools;

public class GoodsDetail extends Effect {

    public String name;

    public String spec;

    public double price;

    public double unit;

    public double quantity;

    public double amount;

    public String getCalcAmount() {
        return Tools.calcAmount(String.valueOf(quantity), String.valueOf(price));
    }
}
