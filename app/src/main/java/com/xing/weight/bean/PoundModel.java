package com.xing.weight.bean;

/**
 *
 * 磅单模板
 */
public class PoundModel {

    public PoundType type; //字段类型

    public int inputType = 0; // 0:文本  1:数字  2：电话  3:多好输入

    public int lenght = 20; //输入内容长度限制

    public String name; //名称

    public String value; //输入的内容

    public String hint; //提示信息

    public PoundModel(String name) {
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
        OUTTIME(8), //出场时间
        TOTALWEIGHT(9), //总重
        REALWEIGHT(10), //净重
        DISCOUNT(11), //折扣
        PRICE(12), //单价
        TOTALPRICE(13), //总价
        PERSON(14), //司磅员
        RECEIVENAME(15), //接收方
        DRIVER(16), //司机
        REMARKS(17), //备注

        ADD(20); //按钮

        private final int value;

        private PoundType(int value) {
            this.value = value;
        }

    }
}
