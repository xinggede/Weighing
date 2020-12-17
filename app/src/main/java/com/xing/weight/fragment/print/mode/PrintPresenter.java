package com.xing.weight.fragment.print.mode;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.bean.request.RequestList;
import com.xing.weight.fragment.main.my.mode.MyModel;

import java.util.ArrayList;
import java.util.List;

public class PrintPresenter extends BasePresenter<PrintContract.View, PrintContract.Model> {

    private List<PrinterInfo> prints = new ArrayList<>();
    private List<CustomerInfo> customer = new ArrayList<>();
    private List<TemplateInfo> temps = new ArrayList<>();

    public List<PrinterInfo> getSavePrints() {
        return prints;
    }

    public void setSavePrints(List<PrinterInfo> list) {
        if(list != null){
            prints.clear();
            prints.addAll(list);
        }
    }

    public List<CustomerInfo> getSaveCustomer() {
        return customer;
    }

    public List<TemplateInfo> getSaveTemps() {
        return temps;
    }


    @Override
    protected PrintContract.Model createModel() {
        return new PrintModel();
    }


    public void getPrints(boolean show) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.pageSize = 1000;
        requestHttp(mModel.getMainApi().getPrinter(requestList),
                valueInfo -> {
                    prints.clear();
                    prints.addAll(valueInfo.records);
                    getView().onHttpResult(true, show?1:0, valueInfo);
                },false);
    }


    public void addPound(PoundInfo poundInfo) {
        requestHttp(mModel.getMainApi().addPound(poundInfo),
                valueInfo -> {
                    getView().onHttpResult(true, 4, valueInfo);
                });
    }

}
