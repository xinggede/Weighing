package com.xing.weight.fragment.bill.mode;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.bean.request.RequestList;
import com.xing.weight.fragment.main.my.mode.MyModel;
import com.xing.weight.server.http.response.ResponseTransformer;
import com.xing.weight.server.http.schedulers.RetryWithDelay;
import com.xing.weight.server.http.schedulers.SchedulerProvider;
import com.xing.weight.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

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

    public void editPound(PoundInfo poundInfo) {
        requestHttp(mModel.getMainApi().updatePound(poundInfo),
                valueInfo -> {
                    getView().onHttpResult(true, 4, valueInfo);
                });
    }

    public void getPounds(int index, boolean show) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = index;
        requestHttp(mModel.getMainApi().getPounds(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 0, valueInfo);
                }, e -> {
                    getView().onHttpResult(false, 0, null);
                    onError(e);
                    onComplete(show);
                }, show);
    }

    public void getPoundsMore(int index) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = index;
        requestHttp(mModel.getMainApi().getPounds(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 1, valueInfo);
                }, e -> {
                    getView().onHttpResult(false, 1, null);
                    onError(e);
                }, false);
    }

    public CompanyInfo getCompanyInfo() {
        return new MyModel().getCompanyInfo();
    }


    private PublishSubject<CharSequence> searchObserver;

    public void searchPoundRecord(EditText etSearch) {
        searchObserver = PublishSubject.create();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchObserver.onNext(s);
                if (TextUtils.isEmpty(s)) {
                    getView().onHttpResult(false, 10, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchPounds();
    }

    private void searchPounds() {
        Disposable disposable = searchObserver.debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        return charSequence.toString().trim().length() > 0;
                    }
                })
                .switchMap(new Function<CharSequence, ObservableSource<PageList<PoundInfo>>>() {
                    @Override
                    public ObservableSource<PageList<PoundInfo>> apply(CharSequence charSequence) throws Exception {
                        RequestList requestList = new RequestList();
                        requestList.pageIndex = 0;
                        requestList.keyword = charSequence.toString();
                        return mModel.getMainApi().getPounds(requestList)
                                .compose(ResponseTransformer.handleResult())
                                .compose(SchedulerProvider.getInstance().applySchedulers())
                                .retryWhen(new RetryWithDelay(2, 3000));
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageList<PoundInfo>>() {
                    @Override
                    public void accept(PageList<PoundInfo> result) throws Exception {
                        if (getView() != null) {
                            getView().onHttpResult(true, 10, result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Tools.loge("search:" + throwable.getMessage());
                        searchPounds();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Tools.loge("search: done");
                    }
                });
        addDispose(disposable);
    }

    @Override
    public void detachView() {
        if (searchObserver != null) {
            searchObserver.onComplete();
        }
        super.detachView();
    }
}
