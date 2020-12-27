package com.xing.weight.fragment.main.manage;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.PageList;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;
import com.xing.weight.fragment.main.manage.mode.MyCustomAdapter;
import com.xing.weight.view.CusSearchText;
import com.xing.weight.view.SpaceItemDecoration;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_BOTTOM;
import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_TOP;

public class MyCustomListFragment extends BaseFragment<ManagePresenter> implements ManageContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.et_search)
    CusSearchText etSearch;
    private int page = 1, deleteIndex;
    private QMUIPullLayout.PullAction mPullAction;
    private boolean isChoose = false;
    private MyCustomAdapter mAdapter;

    public MyCustomListFragment() {
    }

    public MyCustomListFragment(boolean isChoose) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.DATA, isChoose);
        setArguments(bundle);
    }

    @Override
    protected ManagePresenter onLoadPresenter() {
        return new ManagePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_customer;
    }

    @Override
    protected void initView(View view) {
        if (getArguments() != null) {
            isChoose = getArguments().getBoolean(Constants.DATA, false);
        }
        pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                mPullAction = pullAction;
                if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
                    onRefreshData();
                } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {
                    onLoadMore();
                }
            }
        });

        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delete(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                delete(selected.getAdapterPosition());
                swipeAction.clear();
            }
        });

        swipeAction.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        recyclerView.addItemDecoration(new SpaceItemDecoration(QMUIDisplayHelper.dp2px(getContext(), 10), 2));
        mAdapter = new MyCustomAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((itemView, pos) -> {
            startFragment(new MyCustomAddFragment(mAdapter.getItem(pos)));
        });
        callback();
        onDataLoaded();
    }

    private void onDataLoaded() {
        page = 1;
        mPresenter.getCustom(page, true);
    }

    private void onRefreshData() {
        page = 1;
        mPresenter.getCustom(page, false);
    }

    private void onLoadMore() {
        page++;
        mPresenter.getCustomMore(page);
    }

    private void delete(int position) {
        deleteIndex = position;
        CustomerInfo info = mAdapter.getItem(position);
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(info.comname)
                .setMessage("确定要删除该客户吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        mPresenter.deleteCustom(info.id);
                    }
                }).create(R.style.DialogTheme2).show();
    }


    @OnClick({R.id.ib_back, R.id.ib_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                popBackStack();
                break;
            case R.id.ib_add:
                startFragment(new MyCustomAddFragment(null));
                break;
        }
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(MyCustomAddFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {  //该方法只会在界面显示的时候才调用（主线程）
                boolean value = (boolean) effect.getValue(MyCustomAddFragment.class.getName());
                if (value) {
                    onDataLoaded();
                }
            }
        });
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (mPullAction != null) {
            pullLayout.finishActionRun(mPullAction);
        }
        if (success) {
            if (code == 0) {
                PageList<CustomerInfo> pageList = (PageList<CustomerInfo>) data;
                mAdapter.setData(pageList.records);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
                if(mAdapter.getItemCount()== 0 && mPullAction == null){
                    startFragment(new MyCustomAddFragment(null));
                }
            } else if (code == 1) {
                PageList<CustomerInfo> pageList = (PageList<CustomerInfo>) data;
                mAdapter.append(pageList.records);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
            } else if (code == 2) {
                mAdapter.remove(deleteIndex);
            }
        }
    }
}
