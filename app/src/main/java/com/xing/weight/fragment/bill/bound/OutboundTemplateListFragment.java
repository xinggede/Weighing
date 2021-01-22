package com.xing.weight.fragment.bill.bound;

import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.bill.mode.TemplateAdapter;
import com.xing.weight.fragment.bill.pound.PoundTemplateEditFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_BOTTOM;
import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_TOP;

public class OutboundTemplateListFragment extends BaseFragment<BillPresenter> implements BillContract.View {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;

    private QMUIPullLayout.PullAction mPullAction;
    private int page = 1, deleteIndex;
    private TemplateAdapter mAdapter;

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_template;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("出库单模板列表");
        topbar.addLeftBackImageButton().setOnClickListener(v -> popBackStack());
        topbar.addRightImageButton(R.mipmap.icon_add, R.id.topbar_right_add_button).setOnClickListener((v) -> {
            startFragment(new PoundTemplateEditFragment(null));
        });

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
                if (action == mAdapter.mDeleteAction) {
                    delete(selected.getAdapterPosition());
                } else {
                }
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

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new TemplateAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                startFragment(new OutboundTemplateEditFragment(mAdapter.getItem(pos)));
            }
        });
        callback();
        onDataLoaded();
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(OutboundTemplateEditFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {
                boolean value = (boolean) effect.getValue(OutboundTemplateEditFragment.class.getName());
                if (value) {
                    onDataLoaded();
                }
            }
        });
    }

    private void onDataLoaded() {
        page = 1;
        mPresenter.getTemplate(2, true);
    }

    private void onRefreshData() {
        page = 1;
        mPresenter.getTemplate(2, false);
    }

    private void onLoadMore() {
        page++;
        mPresenter.getTemplateMore(2, page);
    }

    private void delete(int position) {
        deleteIndex = position;
        TemplateInfo printerInfo = mAdapter.getItem(position);
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(printerInfo.name)
                .setMessage("确定要删除该模版吗？")
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
                        mPresenter.deleteTemplate(printerInfo.id);
                    }
                }).create(R.style.DialogTheme2).show();
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (mPullAction != null) {
            pullLayout.finishActionRun(mPullAction);
        }
        if (code == 0) {
            if (success) {
                PageList<TemplateInfo> pageList = (PageList<TemplateInfo>) data;
                mAdapter.setData(pageList.records);

                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
                if (mAdapter.getItemCount() == 0 && mPullAction == null) {
                    startFragment(new OutboundTemplateEditFragment(null));
                }
            }
        } else if (code == 1) {
            if (success) {
                PageList<TemplateInfo> pageList = (PageList<TemplateInfo>) data;
                mAdapter.append(pageList.records);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
            }
        } else if (code == 2) {
            if (success) {
                mAdapter.remove(deleteIndex);
            }
        }
    }
}
