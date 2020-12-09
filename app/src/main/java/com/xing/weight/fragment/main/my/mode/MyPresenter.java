package com.xing.weight.fragment.main.my.mode;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.bean.request.RequestList;

public class MyPresenter extends BasePresenter<MyContract.View, MyContract.Model> {

    @Override
    protected MyContract.Model createModel() {
        return new MyModel();
    }

    public int getCompanyId(){
        return mModel.getCompanyId();
    }

    public void getCompanyInfo(){
        int id = getCompanyId();
        if(id == -1){
            return;
        }
        requestHttp(mModel.getMainApi().getCompanyInfo(id),
                valueInfo -> {
                    getView().onHttpResult(true,0, valueInfo);
                },true);
    }

    public void addCompanyInfo(CompanyInfo companyInfo){
        requestHttp(getCompanyId() == -1?mModel.getMainApi().addCompanyInfo(companyInfo):mModel.getMainApi().updateCompanyInfo(companyInfo),
                valueInfo -> {
                    mModel.saveCompanyInfo(valueInfo);
                    getView().onHttpResult(true,1, valueInfo);
                },true);
    }


    public void addPrinter(PrinterInfo printerInfo){
        requestHttp(mModel.getMainApi().addPrinter(printerInfo),
                o -> {
                    getView().onHttpResult(true,3, o);
                });
    }

    public void updatePrinter(PrinterInfo printerInfo){
        requestHttp(mModel.getMainApi().updatePrinter(printerInfo),
                o -> {
                    getView().onHttpResult(true,3, o);
                });
    }

    public void getPrinter(boolean show){
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.pageSize = 100;
        requestHttp(mModel.getMainApi().getPrinter(requestList),
                o -> {
                    getView().onHttpResult(true,0, o);
                },e -> {
                    getView().onHttpResult(false, 0, null);
                    onError(e);
                    onComplete(show);
                }, show);
    }

    public void deletePrinter(int id){
        requestHttp(mModel.getMainApi().deletePrinter(id),
                o -> {
                    getView().onHttpResult(true,1, o);
                });
    }




}
