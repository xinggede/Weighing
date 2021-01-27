package com.xing.weight.fragment.main;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentPagerAdapter;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.fragment.main.home.HomeFragment;
import com.xing.weight.fragment.main.manage.ManageFragment;
import com.xing.weight.fragment.main.my.CompanyInfoFragment;
import com.xing.weight.fragment.main.my.MyFragment;
import com.xing.weight.server.http.download.DownLoadListener;
import com.xing.weight.server.http.download.DownloadManager;
import com.xing.weight.util.Tools;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;

import static com.qmuiteam.qmui.arch.SwipeBackLayout.DRAG_DIRECTION_NONE;

public class MainFragment extends BaseFragment<MainPresenter> implements MainContract.View {

    @BindView(R.id.pager)
    QMUIViewPager viewPager;
    @BindView(R.id.tabs)
    QMUITabSegment tabs;

    QMUIFragmentPagerAdapter pagerAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view) {
        QMUITabBuilder builder = tabs.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.2f)
                .setTextSize(QMUIDisplayHelper.sp2px(getContext(), 12), QMUIDisplayHelper.sp2px(getContext(), 14))
                .setDynamicChangeIconColor(false);

        QMUITab component = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_home))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_home_select))
                .setText("首页")
                .build(getContext());
        QMUITab util = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_manage))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_manage_select))
                .setText("管理")
                .build(getContext());
        QMUITab lab = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_my))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_my_select))
                .setText("我的")
                .build(getContext());

        tabs.addTab(component)
                .addTab(util)
                .addTab(lab);
        initPagers();


        if(!mPresenter.isCompany()){
            showCompany();
        } else {
            mPresenter.checkVersion(Tools.getVerCode(getContext()));
        }
    }

    private void initPagers() {
        pagerAdapter = new QMUIFragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public QMUIFragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new HomeFragment();

                    case 1:
                        return new ManageFragment();

                    case 2:
                        return new MyFragment();

                    default:
                        return new HomeFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager, false);
    }

    private void showCompany() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("提示")
                .setMessage("部分功能需要完善公司信息才能正常使用!")
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
                        startFragment(new CompanyInfoFragment());
                    }
                }).create(R.style.DialogTheme2).show();
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


    @Override
    protected int getDragDirection(@NonNull SwipeBackLayout swipeBackLayout, @NonNull SwipeBackLayout.ViewMoveAction viewMoveAction, float downX, float downY, float dx, float dy, float slopTouch) {
        return DRAG_DIRECTION_NONE;
    }

    @Override
    public Object onLastFragmentFinish() {
        return null;
    }

    @Override
    public void onAppUpdate(String url) {
        if(!TextUtils.isEmpty(url)){
            showUpdate(url);
        } else {
            showToast("无新版本");
        }
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
