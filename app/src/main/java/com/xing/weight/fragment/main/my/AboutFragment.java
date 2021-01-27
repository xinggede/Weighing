package com.xing.weight.fragment.main.my;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.EmptyPresenter;
import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import com.xing.weight.server.http.download.DownLoadListener;
import com.xing.weight.server.http.download.DownloadManager;
import com.xing.weight.util.Tools;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutFragment extends BaseFragment<MainPresenter> implements MainContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_ver)
    TextView tvVer;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("关于");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        tvVer.setText(Tools.getVerCode(getContext()) + " - V" + Tools.getVersionName(getContext()));
    }

    @OnClick({R.id.re_ver, R.id.re_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_ver:
                mPresenter.checkVersion(Tools.getVerCode(getContext()));
                break;

            case R.id.re_phone:
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onAppUpdate(String url) {
        if(!TextUtils.isEmpty(url)){
            showUpdate(url);
        } else {
            showToast("无新版本");
        }
    }

    private void showUpdate(String url) {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("升级提醒")
                .setMessage("检测到新版本，是否升级？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        downloadApk(url);
                        showProgress(url);

                    }
                }).create(R.style.DialogTheme2).show();
    }

    private QMUIProgressBar progressBar;
    private QMUIDialog progressDialog;

    private void showProgress(String url){
        progressDialog = new QMUIDialog.CustomDialogBuilder(getActivity()).setLayout(R.layout.dialog_update_progress).setTitle("下载中").addAction(0, "停止下载", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                DownloadManager.cancelDownload(url);
            }
        }).create(R.style.DialogTheme2);
        progressBar = progressDialog.findViewById(R.id.progressBar);
        progressBar.setType(QMUIProgressBar.TYPE_ROUND_RECT);
        progressDialog.show();
    }

    private void downloadApk(String url){
        String path = Tools.getDownloadPath(getContext()) + File.separator + "weigh.apk";
        DownloadManager.downLoadApk(url, path, new DownLoadListener() {
            @Override
            public void onDownLoadSuccess(String filePath) {
                progressDialog.dismiss();
                progressBar.setProgress(0);
                Tools.installAPK(getContext(),filePath);
            }

            @Override
            public void onDownloadProgress(int progress) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onDownLoadError(String message) {
                progressDialog.dismiss();
                progressBar.setProgress(0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        super.onDestroyView();
    }
}
