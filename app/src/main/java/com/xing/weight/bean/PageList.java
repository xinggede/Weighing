package com.xing.weight.bean;

import java.util.List;

public class PageList<T> {

    public int total;
    public List<T> records;
    public int pageIndex = 1;
    public int pageSize = 20;
    public int pageNum;


}
