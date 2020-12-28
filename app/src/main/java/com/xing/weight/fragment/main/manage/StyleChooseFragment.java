package com.xing.weight.fragment.main.manage;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.StyleInfo;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;
import com.xing.weight.fragment.main.manage.mode.StyleAdapter;
import com.xing.weight.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_BOTTOM;
import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_TOP;

public class StyleChooseFragment extends BaseFragment<ManagePresenter> implements ManageContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    private int page = 1, type;
    private QMUIPullLayout.PullAction mPullAction;
    private StyleAdapter mAdapter;

    public StyleChooseFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.UI_TYPE, type);
        setArguments(bundle);
    }

    @Override
    protected ManagePresenter onLoadPresenter() {
        return new ManagePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        type = getArguments().getInt(Constants.UI_TYPE);
        topbar.addLeftBackImageButton().setOnClickListener(v->{popBackStack();});
        topbar.setTitle(R.string.bill_text6);
        pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                mPullAction = pullAction;
                if (pullAction.getPullEdge() == PULL_EDGE_TOP) {
                    onRefreshData();
                } else if (pullAction.getPullEdge() == PULL_EDGE_BOTTOM) {
                    onLoadMore();
                }
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        recyclerView.addItemDecoration(new SpaceItemDecoration(QMUIDisplayHelper.dp2px(getContext(), 10), 2));
        mAdapter = new StyleAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((itemView, pos) -> {
            Map<String,Object> map = new HashMap<>();
            map.put(getClass().getName(), mAdapter.getItem(pos));
            notifyEffect(new MapEffect(map));
            popBackStack();
        });
        onDataLoaded();
    }

    private void onDataLoaded() {
        page = 1;
        mPresenter.getStyle(page, type, true);
    }

    private void onRefreshData() {
        page = 1;
        mPresenter.getStyle(page, type,false);
    }

    private void onLoadMore() {
        page++;
        mPresenter.getStyleMore(page, type);
    }


    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (mPullAction != null) {
            pullLayout.finishActionRun(mPullAction);
        }
        if (success) {
            if (code == 0) {
                PageList<StyleInfo> pageList = (PageList<StyleInfo>) data;
                mAdapter.setData(pageList.records);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
            } else if (code == 1) {
                PageList<StyleInfo> pageList = (PageList<StyleInfo>) data;
                mAdapter.append(pageList.records);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
            }
        }
    }
}
