package com.xing.weight.fragment.bill.mode;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.request.RequestList;

import java.util.ArrayList;
import java.util.List;

public class BillPresenter extends BasePresenter<BillContract.View, BillContract.Model> {

    private List<GoodsDetail> goods = new ArrayList<>();
    private List<CustomerInfo> customer = new ArrayList<>();

    public List<GoodsDetail> getSaveGoods() {
        return goods;
    }

    public List<CustomerInfo> getSaveCustomer() {
        return customer;
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
                    getView().onHttpResult(true, 0, valueInfo);
                });
    }

    public void getCustom() {
        RequestList requestList = new RequestList();
        requestList.pageIndex = 1;
        requestList.pageSize = 1000;
        requestHttp(mModel.getMainApi().getCustomer(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 1, valueInfo);
                });
    }


    @Override
    public void detachView() {
        super.detachView();
    }
}
