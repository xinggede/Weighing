package com.xing.weight.fragment.main.manage.mode;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.request.RequestList;
import com.xing.weight.server.http.response.ResponseTransformer;
import com.xing.weight.server.http.schedulers.RetryWithDelay;
import com.xing.weight.server.http.schedulers.SchedulerProvider;
import com.xing.weight.util.Tools;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

public class ManagePresenter extends BasePresenter<ManageContract.View, ManageContract.Model> {


    @Override
    protected ManageContract.Model createModel() {
        return new ManageModel();
    }


    public void getGoods(int index, boolean show) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = index;
        requestHttp(mModel.getMainApi().getGoods(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 0, valueInfo);
                }, e -> {
                    getView().onHttpResult(false, 0, null);
                    onError(e);
                    onComplete(show);
                }, show);
    }

    public void getGoodsMore(int index) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = index;
        requestHttp(mModel.getMainApi().getGoods(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 1, valueInfo);
                }, e -> {
                    onError(e);
                    getView().onHttpResult(false, 1, null);
                }, false);
    }

    public void deleteGoods(int id) {
        requestHttp(mModel.getMainApi().deleteGoods(id),
                o -> {
                    getView().onHttpResult(true, 2, o);
                });
    }

    public void addGoods(GoodsDetail goodsDetail) {
        requestHttp(mModel.getMainApi().addGoods(goodsDetail),
                o -> {
                    getView().onHttpResult(true, 3, o);
                });
    }

    public void updateGoods(GoodsDetail goodsDetail) {
        requestHttp(mModel.getMainApi().updateGoods(goodsDetail),
                o -> {
                    getView().onHttpResult(true, 4, o);
                });
    }


    public void getCustom(int index, boolean show) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = index;
        requestHttp(mModel.getMainApi().getCustomer(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 0, valueInfo);
                }, e -> {
                    getView().onHttpResult(false, 0, null);
                    onError(e);
                    onComplete(show);
                }, show);
    }

    public void getCustomMore(int index) {
        RequestList requestList = new RequestList();
        requestList.pageIndex = index;
        requestHttp(mModel.getMainApi().getCustomer(requestList),
                valueInfo -> {
                    getView().onHttpResult(true, 1, valueInfo);
                }, e -> {
                    onError(e);
                    getView().onHttpResult(false, 1, null);
                }, false);
    }

    public void deleteCustom(int id) {
        requestHttp(mModel.getMainApi().deleteCustomer(id),
                o -> {
                    getView().onHttpResult(true, 2, o);
                });
    }

    public void addCustom(CustomerInfo customerInfo) {
        requestHttp(mModel.getMainApi().addCustomer(customerInfo),
                o -> {
                    getView().onHttpResult(true, 3, o);
                });
    }

    public void updateCustom(CustomerInfo customerInfo) {
        requestHttp(mModel.getMainApi().updateCustomer(customerInfo),
                o -> {
                    getView().onHttpResult(true, 4, o);
                });
    }


    private PublishSubject<CharSequence> searchObserver;

    public void searchGoodsData(EditText etSearch) {
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
        searchGoods();
    }

    private void searchGoods() {
        Disposable disposable = searchObserver.debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        return charSequence.toString().trim().length() > 0;
                    }
                })
                .switchMap(new Function<CharSequence, ObservableSource<PageList<GoodsDetail>>>() {
                    @Override
                    public ObservableSource<PageList<GoodsDetail>> apply(CharSequence charSequence) throws Exception {
                        RequestList requestList = new RequestList();
                        requestList.pageIndex = 0;
                        requestList.keyword = charSequence.toString();
                        return mModel.getMainApi().getGoods(requestList)
                                .compose(ResponseTransformer.handleResult())
                                .compose(SchedulerProvider.getInstance().applySchedulers())
                                .retryWhen(new RetryWithDelay(2, 3000));
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageList<GoodsDetail>>() {
                    @Override
                    public void accept(PageList<GoodsDetail> result) throws Exception {
                        if (getView() != null) {
                            getView().onHttpResult(true, 10, result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Tools.loge("search:" + throwable.getMessage());
                        searchGoods();
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
