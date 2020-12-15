package com.xing.weight.fragment.bill.mode;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.bean.request.RequestList;
import com.xing.weight.fragment.main.my.mode.MyModel;

import java.util.ArrayList;
import java.util.List;

public class BillPresenter extends BasePresenter<BillContract.View, BillContract.Model> {

    private List<GoodsDetail> goods = new ArrayList<>();
    private List<CustomerInfo> customer = new ArrayList<>();
    private List<TemplateInfo> temps = new ArrayList<>();

    public List<GoodsDetail> getSaveGoods() {
        return goods;
    }

    public List<CustomerInfo> getSaveCustomer() {
        return customer;
    }

    public List<TemplateInfo> getSaveTemps() {
        return temps;
    }


    @Override
    protected BillContract.Model createModel() {
        return new BillModel();
    }


    public void getGoods() {
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.pageSize = 1000;
        requestHttp(mModel.getMainApi().getGoods(requestList),
                valueInfo -> {
                    goods.clear();
                    goods.addAll(valueInfo.records);
                    getView().onHttpResult(true, 3, valueInfo);
                });
    }

    public void getCustom() {
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.pageSize = 1000;
        requestHttp(mModel.getMainApi().getCustomer(requestList),
                valueInfo -> {
                    customer.clear();
                    customer.addAll(valueInfo.records);
                    getView().onHttpResult(true, 2, valueInfo);
                });
    }


    public void getTemplateChoose(int type, boolean show) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.type = type;
        requestList.pageSize = 1000;
        requestHttp(mModel.getMainApi().getTemplate(requestList),
                valueInfo -> {
                    temps.clear();
                    temps.addAll(valueInfo.records);
                    getView().onHttpResult(true, show?1:0, valueInfo);
                });
    }

    /**
     * 1磅单;2出库单
     *
     * @param type
     */
    public void getTemplate(int type, boolean show) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.type = type;
        requestHttp(mModel.getMainApi().getTemplate(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 0, valueInfo);
                }, e -> {
                    getView().onHttpResult(false, 0, null);
                    onError(e);
                    onComplete(show);
                }, show);
    }

    public void getTemplateMore(int type, int index) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = index;
        requestList.type = type;
        requestHttp(mModel.getMainApi().getTemplate(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 1, valueInfo);
                }, e -> {
                    getView().onHttpResult(false, 1, null);
                    onError(e);
                }, false);
    }

    public void deleteTemplate(int id) {
        requestHttp(mModel.getMainApi().deleteTemplate(id),
                valueInfo -> {
                    getView().onHttpResult(true, 2, valueInfo);
                });
    }

    public void addTemplate(TemplateInfo templateInfo, boolean isAdd) {
        requestHttp(isAdd ? mModel.getMainApi().addTemplate(templateInfo) : mModel.getMainApi().updateTemplate(templateInfo),
                valueInfo -> {
                    getView().onHttpResult(true, 0, valueInfo);
                });
    }

    public void addPound(PoundInfo poundInfo) {
        requestHttp(mModel.getMainApi().addPound(poundInfo),
                valueInfo -> {
                    getView().onHttpResult(true, 4, valueInfo);
                });
    }

    public CompanyInfo getCompanyInfo() {
        return new MyModel().getCompanyInfo();
    }


    @Override
    public void detachView() {
        super.detachView();
    }
}
