package com.xing.weight.bean;

import java.util.List;

public class QueryPrintResult {

    public int return_code;
    public String return_msg;

    public List<PrintBean> return_data;


    public static class PrintBean {
        public String cus_orderid;
        public String order_id;
        public int order_status;
        public String result_msg;
        public String result_code;
        public String device_id;
    }
}
