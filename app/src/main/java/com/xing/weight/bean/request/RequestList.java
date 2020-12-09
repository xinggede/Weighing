package com.xing.weight.bean.request;

public class RequestList {

    public int pageIndex = 1;
    public int pageSize = 20;
    public PageSorts pageSorts;
    public String keyword;

    public static class PageSorts {
        public String column;
        public boolean asc;
    }

}
