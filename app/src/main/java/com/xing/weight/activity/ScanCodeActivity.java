package com.xing.weight.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseActivity;
import com.xing.weight.base.Constants;
import com.xing.weight.base.EmptyPresenter;
import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.util.ImgUtils;
import com.xing.weight.util.Tools;
import com.yingmei.scancode.core.QRCodeView;
import com.yingmei.scancode.zxing.ZXingView;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.xing.weight.base.Constants.RC_CAMERA_PERM;

public class ScanCodeActivity extends BaseActivity<EmptyPresenter> implements BaseContract.View, QRCodeView.Delegate {

    @BindView(R.id.zxingview)
    ZXingView zxingview;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.bt_open_light)
    Button btOpenLight;
    /**
     * 是否扫描图片
     */
    private boolean isScanImg = false;

    @Override
    protected EmptyPresenter onLoadPresenter() {
        return new EmptyPresenter();
    }

    @Override
    public int getRootViewId() {
        return R.layout.activity_scan_code;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        zxingview.setDelegate(this);
        topbar.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        topbar.setTitle("扫码");
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {

    }

    private boolean hasPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (hasPermission()) {
            if (!isScanImg) {
                zxingview.startCamera();
                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
            } else {
                zxingview.showScanRect();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.perm_camera), RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zxingview.stopSpotAndHiddenRect(); // 关闭摄像头预览，并且隐藏扫描框
    }

    @Override
    protected void onDestroy() {
        zxingview.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void openFileChooseProcess() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//        registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
//            Tools.loge("result= " + result);
//        }).launch("image/*");

        registerForActivityResult(new ActivityResultContract<String, Uri>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, String input) {
                return new Intent(Intent.ACTION_PICK)
                        .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, input);
            }

            @Override
            public Uri parseResult(int resultCode, @Nullable Intent intent) {
                if (intent == null || resultCode != Activity.RESULT_OK) {
                    return null;
                };
                return intent.getData();
            }
        },result ->{
            Tools.loge("result= " + result);
            isScanImg = true;
            zxingview.scanFile(ImgUtils.getPath(this, result));
        }).launch("image/*");
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        hideLoading();
        zxingview.stopSpotAndHiddenRect();
        if (!TextUtils.isEmpty(result) && result.length() > 9) {
            zxingview.stopCamera();
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA, result);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showToast("请扫描正确的打印机二维码");
            isScanImg = false;
//            zxingview.stopCamera();
//            zxingview.startCamera();
            zxingview.startSpotAndShowRect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tools.loge("onActivityResult: " + resultCode);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasPermission()) {
                cameraTask();
            } else {
                showToast("权限申请失败,无法使用扫码功能");
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        /*String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }*/
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        if(hasPermission()){
            showToast("摄像头打开失败,请稍候重试");
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @OnClick({R.id.bt_input_code, R.id.bt_scan_image, R.id.bt_open_light})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_input_code:
                onBackPressed();
                break;

            case R.id.bt_scan_image:
                openFileChooseProcess();
                break;

            case R.id.bt_open_light:
                if (zxingview.switchFlashlight()) {
                    btOpenLight.setText("关闭手电筒");
                    btOpenLight.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.mipmap.icon_s_light_open), null, null);
                } else {
                    btOpenLight.setText("打开手电筒");
                    btOpenLight.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.mipmap.icon_s_light_close), null, null);
                }
                break;
        }
    }

}
