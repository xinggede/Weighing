package com.xing.weight.fragment.main.my;

import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.RecyclerViewHolder;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import com.xing.weight.fragment.main.my.mode.PrintAdapter;
import com.xing.weight.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class PrintManageFragment extends BaseFragment<MainPresenter> implements MainContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    private PrintAdapter mAdapter;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print_manage;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle(R.string.print_manage);
        topbar.addLeftBackImageButton().setOnClickListener((v)->{
            popBackStack();
        });
        topbar.addRightImageButton(R.mipmap.icon_add, R.id.topbar_right_add_button).setOnClickListener((v)->{
            startFragment(new PrintAddFragment());
        });

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
                if(action == mAdapter.mDeleteAction){
                    mAdapter.remove(selected.getAdapterPosition());
                }else{
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

        recyclerView.addItemDecoration(new SpaceItemDecoration(QMUIDisplayHelper.dp2px(getContext(),10), 2));
        mAdapter = new PrintAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        onDataLoaded();
    }

    private void onDataLoaded() {
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


}
