package com.xing.weight.fragment.print.mode;

import android.content.Context;
import android.graphics.Bitmap;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PaperInfo;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.PrintFile;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.bean.request.RequestList;
import com.xing.weight.fragment.bill.pound.PoundRecordFragment;
import com.xing.weight.fragment.main.my.mode.MyModel;
import com.xing.weight.util.Tools;
import com.yingmei.printsdk.JolimarkPrint;
import com.yingmei.printsdk.bean.DeviceInfo;
import com.yingmei.printsdk.bean.PrintCallback;
import com.yingmei.printsdk.bean.PrintParameters;
import com.yingmei.printsdk.bean.SearchCallback;
import com.yingmei.printsdk.bean.TransType;
import com.yingmei.printsdk.core.States;

import java.util.ArrayList;
import java.util.List;

public class PrintPresenter extends BasePresenter<PrintContract.View, PrintContract.Model> implements SearchCallback, PrintCallback {

    private List<PrinterInfo> prints = new ArrayList<>();
    private List<CustomerInfo> customer = new ArrayList<>();
    private List<TemplateInfo> temps = new ArrayList<>();
    private List<PaperInfo> papers = new ArrayList<>();

    public List<PaperInfo> getPaperList(){
        return papers;
    }

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


    public void print(PrintFile printFile) {
        requestHttp(mModel.getMainApi().printFile(printFile),
                o -> {
                    getView().onHttpResult(true, 6, o);
                },false);
    }

    public void queryPrintResult(String orderId){
        PrintFile printFile = new PrintFile();
        printFile.ordertype = 1;
        printFile.type = 2;
        printFile.orderid = orderId;

        requestHttp(mModel.getMainApi().printResult(printFile),
                o -> {
                    getView().onHttpResult(true, 7, o);
                },false);
    }

    public void printLocal(PrintFile printFile) {
        requestHttp(mModel.getMainApi().printFile(printFile),
                o -> {
                    Tools.logd("打印同步成功");
//                    getView().onHttpResult(true, 8, o);
                },false);
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

    DeviceInfo deviceInfo;
    String devCode;
    PrintParameters printParameters;
    Context context;

    public DeviceInfo getCurrentDevice(){
        return deviceInfo;
    }

    public void resetDevice(){
        deviceInfo = null;
        devCode = null;
    }

    public void startPrint(Context context, String code, Bitmap bitmap, PaperInfo paperInfo){
        this.context = context;
        printParameters = PrintParameters.createBitmapPrint(bitmap, PrintParameters.PT_DOT24);
        printParameters.paper_height = Tools.stringToDouble2(paperInfo.heigh);
        if(deviceInfo == null){
            searchDevices(context, code);
        } else {
            print();
        }
    }

    private void searchDevices(Context context, String code){
        devCode = code;
        JolimarkPrint.searchDevices(context,20*1000, TransType.TRANS_CLASSIC,this);
    }

    private void print(){
        getView().showMessage("打印中...");
        JolimarkPrint.sendToData(context, deviceInfo,printParameters,this);
    }

    @Override
    public void startDevices() {
        if(getView() == null){
            return;
        }
        Tools.logd("开始搜索打印机");
        getView().showLoading("正在搜索蓝牙打印机");
    }

    @Override
    public void stopDevices(List<DeviceInfo> list) {
        Tools.logd("搜索结束");
        if(deviceInfo == null && list != null){
            for (DeviceInfo deviceInfo : list) {
                if(deviceInfo.getDid().contains(devCode)){
                    this.deviceInfo = deviceInfo;
                    print();
                    break;
                }
            }
        }

        if(deviceInfo == null && getView() != null){
            getView().hideLoading();
            getView().findError();
        }
    }

    @Override
    public void findDevices(DeviceInfo deviceInfo) {
        if(deviceInfo != null){
            return;
        }
        if(deviceInfo.getDid().contains(devCode)){
            Tools.logd("找到设备："+ deviceInfo.getDid());
            this.deviceInfo = deviceInfo;
            JolimarkPrint.stopSearch(false);
            print();
        }
    }

    @Override
    public void printResult(int state, String taskId, String msg) {
        if(getView() == null){
            return;
        }
        getView().hideLoading();
        getView().onPrintResult(state,taskId,msg);
    }

    @Override
    public void onReceiveData(int i, byte[] bytes) {

    }

    @Override
    public void unDispose() {
        super.unDispose();
        JolimarkPrint.stopSearch(true);
        JolimarkPrint.closeConnect();
    }
}
