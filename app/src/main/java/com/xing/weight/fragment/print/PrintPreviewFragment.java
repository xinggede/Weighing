package com.xing.weight.fragment.print;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.AddPoundResultInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.fragment.bill.pound.PoundRecordFragment;
import com.xing.weight.fragment.main.my.PrintAddFragment;
import com.xing.weight.fragment.print.mode.PrintContract;
import com.xing.weight.fragment.print.mode.PrintPresenter;
import com.xing.weight.util.Tools;
import com.yingmei.printsdk.JolimarkPrint;
import com.yingmei.printsdk.bean.DeviceInfo;
import com.yingmei.printsdk.bean.PrintCallback;
import com.yingmei.printsdk.bean.PrintParameters;
import com.yingmei.printsdk.bean.SearchCallback;
import com.yingmei.printsdk.bean.TransType;
import com.yingmei.printsdk.core.States;

import java.util.List;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.xing.weight.base.Constants.RC_LOCATION_PERM;
import static com.xing.weight.base.Constants.RC_WRITE_PERM;

public class PrintPreviewFragment extends BaseFragment<PrintPresenter> implements PrintContract.View, SearchCallback, PrintCallback {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.iv_preview)
    ImageView ivPreview;
    @BindView(R.id.tv_print)
    TextView tvPrint;
    @BindView(R.id.tv_print_type)
    TextView tvPrintType;
    private PrinterInfo printerInfo;
    private QMUIPopup choosePrint, choosePrintType;
    private int printType = 0;
    private AddPoundResultInfo info;
    private Bitmap printBitmap;

    public PrintPreviewFragment(AddPoundResultInfo addPoundResultInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, addPoundResultInfo);
        setArguments(bundle);
    }

    @Override
    protected PrintPresenter onLoadPresenter() {
        return new PrintPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print_preview;
    }

    @Override
    protected void initView(View view) {
        info = getArguments().getParcelable(Constants.DATA);

        topbar.setTitle("打印预览");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        if (info != null) {
            if (info.order != null) {
                showLoading();
                Glide.with(this).asBitmap().load(info.order.url).placeholder(R.mipmap.img_default).error(R.mipmap.img_default)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                ivPreview.setImageBitmap(resource);
                                printBitmap = resource;
                                hideLoading();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                ivPreview.setImageDrawable(placeholder);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                ivPreview.setImageDrawable(errorDrawable);
                                hideLoading();
                            }
                        });
            }
            if (info.printList != null) {
                mPresenter.setSavePrints(info.printList);
                setPrint();
            }
        }
    }

    private void setPrint() {
        if (!mPresenter.getSavePrints().isEmpty()) {
            printerInfo = mPresenter.getSavePrints().get(0);
            tvPrint.setText(printerInfo.name + ":" + printerInfo.devcode);
        } else {
            tvPrint.setText("请先添加一个打印机");
        }
    }

    @OnClick({R.id.tv_print, R.id.tv_print_type, R.id.bt_print, R.id.ib_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_print:
                if (!mPresenter.getSavePrints().isEmpty()) {
                    showChoosePrint(view);
                } else {
                    mPresenter.getPrints(true);
                }
                break;

            case R.id.tv_print_type:
                showChoosePrintType(view);
                break;

            case R.id.bt_print:
                if (printType == 1) {
                    if(printBitmap == null){
                        showToast("打印文件异常");
                        return;
                    }
                    writeTask();
                }
                break;

            default:
                callback();
                startFragment(new PrintAddFragment(null));
                break;
        }
    }

    private boolean hasWritePermission() {
        return EasyPermissions.hasPermissions(getContext(),  Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void startBle(){
        if (!JolimarkPrint.isBluetoothOpen(getContext())) {
            openBle();
        } else {
            if(deviceInfo == null){
                searchDevice(true);
            } else {
                print();
            }
        }
    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    public void writeTask() {
        if (hasWritePermission()) {
            startBle();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.perm_location), RC_LOCATION_PERM, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void openBle() {
            registerForActivityResult(new ActivityResultContract<String, Boolean>() {
                                          @NonNull
                                          @Override
                                          public Intent createIntent(@NonNull Context context, String input) {
                                              return new Intent(input);
                                          }

                                          @Override
                                          public Boolean parseResult(int resultCode, @Nullable Intent intent) {
                                              return JolimarkPrint.isBluetoothOpen(getContext());
                                          }

                                      }, this::searchDevice
            ).launch(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    }

    private void searchDevice(boolean b){
        if(!b){
            openBle();
        } else {
            JolimarkPrint.searchDevices(getContext(),20*1000, TransType.TRANS_BLE,this);
        }
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(PrintAddFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {
                boolean value = (boolean) effect.getValue(PrintAddFragment.class.getName());
                if (value) {
                    mPresenter.getPrints(false);
                }
            }
        });
    }

    private void showChoosePrint(View v) {
        if (choosePrint == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSavePrints());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (choosePrint != null) {
                        choosePrint.dismiss();
                    }
                    printerInfo = (PrinterInfo) adapterView.getItemAtPosition(i);
                    tvPrint.setText(printerInfo.name + ":" + printerInfo.devcode);
                    if(deviceInfo != null){
                        if(!deviceInfo.getDid().equalsIgnoreCase(printerInfo.devcode)){
                            deviceInfo = null;
                        }
                    }
                }
            };
            choosePrint = QMUIPopups.listPopup(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 200),
                    QMUIDisplayHelper.dp2px(getContext(), 300),
                    adapter,
                    onItemClickListener)
                    .bgColor(ContextCompat.getColor(getContext(), R.color.tab_bj))
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        choosePrint.show(v);
    }

    private void showChoosePrintType(View v) {
        if (choosePrintType == null) {
            String[] data = new String[]{"云打印", "蓝牙打印"};
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, data);
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (choosePrintType != null) {
                        choosePrintType.dismiss();
                    }
                    printType = i;
                    tvPrintType.setText(adapterView.getItemAtPosition(i).toString());
                }
            };
            choosePrintType = QMUIPopups.listPopup(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 200),
                    QMUIDisplayHelper.dp2px(getContext(), 300),
                    adapter,
                    onItemClickListener)
                    .bgColor(ContextCompat.getColor(getContext(), R.color.tab_bj))
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        choosePrintType.show(v);
    }

    private void showLocationDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("提示")
                .setMessage("部分手机需要开启定位功能才能正常使用蓝牙打印")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "开启", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        startLocation();
                    }
                }).create(R.style.DialogTheme2).show();
    }

    private void startLocation() {
        registerForActivityResult(new ActivityResultContract<String, Boolean>() {
                                      @NonNull
                                      @Override
                                      public Intent createIntent(@NonNull Context context, String input) {
                                          return new Intent(input);
                                      }

                                      @Override
                                      public Boolean parseResult(int resultCode, @Nullable Intent intent) {
                                          return Tools.isLocationEnable(getContext());
                                      }

                                  }, result -> {
            if(result){
                searchDevice(true);
            } else {
                showToast("定位功能未开启");
            }
        }
        ).launch(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasWritePermission()) {
                startBle();
            } else {
                showToast("位置权限申请失败，无法使用蓝牙打印功能");
            }
        }
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (success) {
            if (code == 0) {
                setPrint();
            } else if (code == 1) {
                if (!mPresenter.getSavePrints().isEmpty()) {
                    showChoosePrint(tvPrint);
                } else {
                    tvPrint.setText("请先添加一个打印机");
                }
            }
        }
    }

    DeviceInfo deviceInfo;

    @Override
    public void startDevices() {
        showLoading("正在搜索蓝牙打印机");
    }

    @Override
    public void stopDevices(List<DeviceInfo> list) {
        if(deviceInfo == null){
            for (DeviceInfo deviceInfo : list) {
                if(deviceInfo.getDid().equalsIgnoreCase(printerInfo.devcode)){
                    this.deviceInfo = deviceInfo;
                    print();
                    break;
                }
            }
        }

        if(deviceInfo == null){
            hideLoading();
            if(!Tools.isLocationEnable(getContext())){
                showLocationDialog();
            } else {
                showToast("未找到蓝牙打印机，请确认打印机是否正常");
            }
        }
    }

    @Override
    public void findDevices(DeviceInfo deviceInfo) {
        if(deviceInfo != null){
            return;
        }
        if(deviceInfo.getDid().equalsIgnoreCase(printerInfo.devcode)){
            this.deviceInfo = deviceInfo;
            JolimarkPrint.stopSearch(false);
            print();
        }
    }

    private void print(){
        showLoading("打印中...");
        PrintParameters printParameters = PrintParameters.createBitmapPrint(printBitmap, PrintParameters.PT_DOT24);
        printParameters.paper_height = 0;
        JolimarkPrint.sendToData(getContext(),deviceInfo,printParameters,this);
    }

    @Override
    public void printResult(int state, String taskId, String msg) {
        hideLoading();
        showToast(msg);
        if(state == States.OK){
            startFragmentAndDestroyCurrent(new PoundRecordFragment());
        }

    }

    @Override
    public void onReceiveData(int cmd, byte[] data) {

    }
}
