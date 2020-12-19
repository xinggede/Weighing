package com.xing.weight.fragment.bill.pound;

import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.RecyclerViewHolder;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.util.Tools;
import com.xing.weight.view.CusItemDecoration;
import com.xing.weight.view.CusSearchText;
import com.xing.weight.view.CustomRootView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_BOTTOM;
import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_TOP;

public class PoundRecordFragment extends BaseFragment<BillPresenter> implements BillContract.View {


    @BindView(R.id.et_search)
    CusSearchText etSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.customView)
    CustomRootView customView;
    private int page = 1;
    private QMUIPullLayout.PullAction mPullAction;
    private BaseRecyclerAdapter<PoundInfo> mAdapter;

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pound_record;
    }

    @Override
    protected void initView(View view) {
        customView.getGlobalBtn().setOnClickListener(v->popBackStack());
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new BaseRecyclerAdapter<PoundInfo>(getContext(), null) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.list_item_bill_record;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, PoundInfo item) {
                holder.setText(R.id.tv_drive_number, item.truckno);
                holder.setText(R.id.tv_time, Tools.getTimeFormatText(item.createDate));
                holder.setText(R.id.tv_weight_man, item.weighman);
                holder.setText(R.id.tv_total_weight, String.valueOf(item.allupweight));
                holder.setText(R.id.tv_drive_name, item.driver);
                holder.setText(R.id.tv_out_time, item.outdate);
                holder.setText(R.id.tv_describe, "该车载重"+item.cargoweight + "吨,"+ item.indate+"首次入场");
            }
        };
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {

            }
        });
        recyclerView.addItemDecoration(new CusItemDecoration(ContextCompat.getColor(getContext(),R.color.divider_color)));
        recyclerView.setAdapter(mAdapter);
        onDataLoaded();
    }

    @OnClick(R.id.ib_back)
    public void onViewClicked() {
        popBackStack();
    }


    private void onDataLoaded() {
        page = 1;
        mPresenter.getPounds(page,true);
    }

    private void onRefreshData() {
        page = 1;
        mPresenter.getPounds(page, false);
    }

    private void onLoadMore() {
        page++;
        mPresenter.getPoundsMore(page);
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (mPullAction != null) {
            pullLayout.finishActionRun(mPullAction);
        }
        if (code == 0) {
            if (success) {
                PageList<PoundInfo> pageList = (PageList<PoundInfo>) data;
                mAdapter.setData(pageList.records);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
            }
        } else if (code == 1) {
            if (success) {
                PageList<PoundInfo> pageList = (PageList<PoundInfo>) data;
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
