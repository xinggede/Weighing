package com.xing.weight.fragment.main.manage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PageList;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;
import com.xing.weight.fragment.main.manage.mode.MyGoodsAdapter;
import com.xing.weight.view.CusSearchText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_BOTTOM;
import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_TOP;

public class MyGoodsListFragment extends BaseFragment<ManagePresenter> implements ManageContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.et_search)
    CusSearchText etSearch;
    @BindView(R.id.lin_title)
    QMUILinearLayout linTitle;

    private MyGoodsAdapter mAdapter;
    private int page = 1, deleteIndex;
    private QMUIPullLayout.PullAction mPullAction;

    private boolean isChoose = false;

    public MyGoodsListFragment() {
    }

    public MyGoodsListFragment(boolean isChoose) {
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
        return R.layout.fragment_my_goods;
    }

    @Override
    protected void initView(View view) {
        if (getArguments() != null) {
            isChoose = getArguments().getBoolean(Constants.DATA, false);
        }
        mPresenter.searchGoodsData(etSearch);
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

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
//        recyclerView.addItemDecoration(new SpaceItemDecoration(QMUIDisplayHelper.dp2px(getContext(), 10), 2));
        mAdapter = new MyGoodsAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((itemView, pos) -> {
            if (isChoose) {
                notifyEffect(mAdapter.getItem(pos));
                popBackStack();
            } else {
                startFragment(new MyGoodsAddFragment(mAdapter.getItem(pos)));
            }

        });
        callback();
        onDataLoaded();
    }

    private void onDataLoaded() {
        page = 1;
        mPresenter.getGoods(page, true);
    }

    private void onRefreshData() {
        page = 1;
        mPresenter.getGoods(page, false);
    }

    private void onLoadMore() {
        page++;
        mPresenter.getGoodsMore(page);
    }

    private void delete(int position) {
        deleteIndex = position;
        GoodsDetail goodsDetail = mAdapter.getItem(position);
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(goodsDetail.name)
                .setMessage("确定要删除该商品吗？")
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
                        mPresenter.deleteGoods(goodsDetail.id);
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
                startFragment(new MyGoodsAddFragment(null));
                break;
        }
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(MyGoodsAddFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {  //该方法只会在界面显示的时候才调用（主线程）
                boolean value = (boolean) effect.getValue(MyGoodsAddFragment.class.getName());
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
        if (code == 0) {
            if (success) {
                PageList<GoodsDetail> pageList = (PageList<GoodsDetail>) data;
                mAdapter.setData(pageList.records);

                linTitle.setVisibility(mAdapter.getItemCount() > 0?View.VISIBLE:View.GONE);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
                if(mAdapter.getItemCount()== 0 && mPullAction == null){
                    startFragment(new MyGoodsAddFragment(null));
                }
            }
        } else if (code == 1) {
            if (success) {
                PageList<GoodsDetail> pageList = (PageList<GoodsDetail>) data;
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
                linTitle.setVisibility(mAdapter.getItemCount() > 0?View.VISIBLE:View.GONE);
            }
        } else if (code == 10) {
            if (success) {
                if (!TextUtils.isEmpty(etSearch.getText())) {
                    mAdapter.saveData();
                }
                PageList<GoodsDetail> pageList = (PageList<GoodsDetail>) data;
                mAdapter.setData(pageList.records);
                if (mAdapter.getItemCount() >= pageList.total) {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP);
                } else {
                    pullLayout.setEnabledEdges(PULL_EDGE_TOP | PULL_EDGE_BOTTOM);
                }
            } else {
                if (mAdapter.getSaveData() == null) {
                    return;
                }
                mAdapter.setData(mAdapter.getSaveData());
                mAdapter.clearSaveData();
            }
            linTitle.setVisibility(mAdapter.getItemCount() > 0?View.VISIBLE:View.GONE);
        }
    }
}
