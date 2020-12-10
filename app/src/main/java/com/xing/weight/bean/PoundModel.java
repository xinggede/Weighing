package com.xing.weight.bean;

/**
 *
 * 磅单模板
 */
public class PoundModel {

    public int type = 0; // 0:文本  1:数字  2：电话

    public int lenght = 20; //输入内容长度限制

    public String name; //名称

    public String value; //输入的内容

    public String hint; //提示信息

    public PoundModel(String name) {
        this.name = name;
    }
}
