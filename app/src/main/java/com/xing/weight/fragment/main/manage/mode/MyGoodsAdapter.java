package com.xing.weight.fragment.main.manage.mode;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xing.weight.R;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.bean.GoodsDetail;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyGoodsAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder> {

    private List<GoodsDetail> mData = new ArrayList<>();
    public final QMUISwipeAction mDeleteAction;
    private BaseRecyclerAdapter.OnItemClickListener mClickListener;

    public MyGoodsAdapter(Context context) {
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textSize(QMUIDisplayHelper.sp2px(context, 14))
                .textColor(Color.WHITE)
                .paddingStartEnd(QMUIDisplayHelper.dp2px(context, 20));

        mDeleteAction = builder.text("删除").backgroundColor(Color.RED).build();
    }

    private List<GoodsDetail> saveData = new ArrayList<>();
    boolean isSave = false;
    public void saveData(){
        if(!isSave){
            saveData.clear();
            saveData.addAll(mData);
            isSave = true;
        }
    }

    public List<GoodsDetail> getSaveData(){
        if(!isSave){
            return null;
        }
        return saveData;
    }

    public void clearSaveData(){
        saveData.clear();
        isSave = false;
    }


    public void setData(@Nullable List<GoodsDetail> list) {
        if (list != null) {
            mData.clear();
            mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void remove(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void add(int pos, GoodsDetail item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void prepend(@NonNull List<GoodsDetail> items) {
        mData.addAll(0, items);
        notifyDataSetChanged();
    }

    public void append(@NonNull List<GoodsDetail> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public GoodsDetail getItem(int position){
        return mData.get(position);
    }


    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_goods, parent, false);
        final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
        vh.addSwipeAction(mDeleteAction);
        if (mClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(view, vh.getAdapterPosition());
                }
            });
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QMUISwipeViewHolder holder, int position) {
        GoodsDetail detail = mData.get(position);
        TextView tvName = holder.itemView.findViewById(R.id.tv_name_or_specs);
        tvName.setText(detail.name+":"+detail.model);

        TextView tvModel = holder.itemView.findViewById(R.id.tv_model_or_price);
        tvModel.setText(detail.type+":"+ detail.pricebuy);

        TextView tvDescribe = holder.itemView.findViewById(R.id.tv_describe);
        tvDescribe.setText(detail.remark);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        mClickListener = listener;
    }
}
