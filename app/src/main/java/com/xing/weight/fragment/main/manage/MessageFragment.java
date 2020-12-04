package com.xing.weight.fragment.main.manage;

import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.RecyclerViewHolder;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MessageFragment extends BaseFragment<MainPresenter> implements MainContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topbar.setTitle("我的订单");

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new BaseRecyclerAdapter<String>(getContext(), null) {
            @Override
            public int getItemLayoutId(int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, String item) {
                holder.setText(android.R.id.text1, item);
            }
        };
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {

            }
        });
        recyclerView.setAdapter(mAdapter);
        onDataLoaded();
    }

    private void onDataLoaded() {
//        List<String> data = new ArrayList<>(Arrays.asList("Helps", "Maintain", "Liver", "Health", "Function", "Supports", "Healthy", "Fat",
//                "Metabolism", "Nuturally", "Bracket", "Refrigerator", "Bathtub", "Wardrobe", "Comb", "Apron", "Carpet", "Bolster", "Pillow", "Cushion"));
//        Collections.shuffle(data);
//        mAdapter.setData(data);
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
