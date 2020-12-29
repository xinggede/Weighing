package com.xing.weight.bean;

import java.util.List;

public class PrintImgResult {

    public int return_code;
    public String return_data;
    public String return_msg;
    public String pordid;

    public List<PrinterState> printer_state;


    public static class PrinterState {
        public String printer_code;
        public int status_code;
        public String status_msg;
    }
}
