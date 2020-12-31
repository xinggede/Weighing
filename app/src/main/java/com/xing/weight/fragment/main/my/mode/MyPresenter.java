package com.xing.weight.fragment.main.my.mode;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.PaperInfo;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.bean.request.RequestList;

import java.util.ArrayList;
import java.util.List;

public class MyPresenter extends BasePresenter<MyContract.View, MyContract.Model> {

    private List<PaperInfo> papers = new ArrayList<>();

    public List<PaperInfo> getPaperList(){
        return papers;
    }

    @Override
    protected MyContract.Model createModel() {
        return new MyModel();
    }

    public String getUserName() {
        return mModel.getUserName();
    }

    public String getUserHead() {
        return mModel.getUserHead();
    }

    public int getCompanyId(){
        return mModel.getCompanyId();
    }

    public String getDueDate() {
        return mModel.getDueDate();
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
        companyInfo.id = getCompanyId();
        requestHttp(getCompanyId() == 0?mModel.getMainApi().addCompanyInfo(companyInfo):mModel.getMainApi().updateCompanyInfo(companyInfo),
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

    public void getPaper(boolean show){
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.pageSize = 1000;
        requestHttp(mModel.getMainApi().getPaper(requestList),
                o -> {
                    papers.clear();
                    papers.addAll(o.records);
                    if(papers.isEmpty()){
                        PaperInfo a4 = new PaperInfo();
                        a4.name = "A4";
                        a4.width = "210";
                        a4.heigh = "290";
                        papers.add(a4);
                    }
                    getView().onHttpResult(true, show?5:4, o);
                });
    }

    public void deletePrinter(int id){
        requestHttp(mModel.getMainApi().deletePrinter(id),
                o -> {
                    getView().onHttpResult(true,1, o);
                });
    }


    public void logout(){
        requestHttp(mModel.getLoginApi().logout(),
                o -> {
                    mModel.exit();
                    getView().onHttpResult(true,0, o);
                }, e->{
                    mModel.exit();
                    getView().onHttpResult(true,0, null);
                    onComplete(true);
                },true);
    }

}
