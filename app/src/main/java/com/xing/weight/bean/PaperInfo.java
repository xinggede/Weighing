package com.xing.weight.bean;

public class PaperInfo {

    public int id;
    public String name;
    public String width;
    public String heigh;
    public int type;
    public String remark;

    @Override
    public String toString() {
        return name + "--" + width + "*" + heigh;
    }
}
