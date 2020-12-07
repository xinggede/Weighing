package com.xing.weight.fragment.main.manage;

import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.effect.QMUIFragmentEffectHandler;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.bean.CustomInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;
import com.xing.weight.fragment.main.manage.mode.MyGoodsAdapter;
import com.xing.weight.util.Tools;
import com.xing.weight.view.CusSearchText;
import com.xing.weight.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MyGoodsListFragment extends BaseFragment<ManagePresenter> implements ManageContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.et_search)
    CusSearchText etSearch;

    private MyGoodsAdapter mAdapter;

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
        pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
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
        mAdapter = new MyGoodsAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((itemView, pos) -> {
            startFragment(new MyGoodsAddFragment(new CustomInfo()));
        });

        onDataLoaded();
    }

    private void onDataLoaded() {
        mPresenter.getGoods();
        List<String> data = new ArrayList<>(Arrays.asList("Helps", "Maintain", "Liver", "Health", "Function", "Supports", "Healthy", "Fat",
                "Metabolism", "Nuturally", "Bracket", "Refrigerator", "Bathtub", "Wardrobe", "Comb", "Apron", "Carpet", "Bolster", "Pillow", "Cushion"));
        Collections.shuffle(data);
        mAdapter.setData(data);
    }

    private void onRefreshData() {
//        List<String> data = new ArrayList<>();
//        long id = System.currentTimeMillis();
//        for(int i = 0; i < 10; i++){
//            data.add("onRefreshData-" + id + "-"+ i);
//        }
//        mAdapter.prepend(data);
//        recyclerView.scrollToPosition(0);
    }

    private void onLoadMore() {
//        List<String> data = new ArrayList<>();
//        long id = System.currentTimeMillis();
//        for(int i = 0; i < 10; i++){
//            data.add("onLoadMore-" + id + "-"+ i);
//        }
//        mAdapter.append(data);
    }


    @OnClick({R.id.ib_back, R.id.ib_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                popBackStack();
                break;
            case R.id.ib_add:
                callback();
                startFragment(new MyGoodsAddFragment(null));
                break;
        }
    }

    private void callback(){
        registerEffect(this, new QMUIFragmentEffectHandler<CustomInfo>() {
            @Override
            public boolean shouldHandleEffect(@NonNull CustomInfo customInfo) { //（调用的线程）返回true才可能执行handleEffect
                return true;
            }

            @Override
            public void handleEffect(@NonNull CustomInfo customInfo) { //该方法只会在界面显示的时候才调用（主线程）
                Tools.logd("handleEffect:"+ customInfo.name);
            }
        });
    }

    @Override
    public void onHttpResult(int code, Object data) {
        if(code == 0){
            List<GoodsDetail> goodsDetails = (List<GoodsDetail>) data;
//            mAdapter.setData(goodsDetails);
        }
    }
}
