package com.xing.weight.fragment.main.manage;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.CustomerInfo;
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

                pullLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullLayout.finishActionRun(pullAction);
                    }
                }, 3000);
            }
        });

        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.remove(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                if (action == mAdapter.mDeleteAction) {
                    mAdapter.remove(selected.getAdapterPosition());
                } else {
                    swipeAction.clear();
                }
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
            startFragment(new MyCustomAddFragment(new CustomerInfo()));
        });

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


    @OnClick({R.id.ib_back, R.id.ib_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                popBackStack();
                break;
            case R.id.ib_add:
                callback();
                startFragment(new MyCustomAddFragment(null));
                break;
        }
    }

    private void callback() {

    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {

    }
}
