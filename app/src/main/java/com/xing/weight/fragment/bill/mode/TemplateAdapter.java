package com.xing.weight.fragment.bill.mode;

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
import com.xing.weight.bean.TemplateInfo;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class TemplateAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder> {

    private List<TemplateInfo> mData = new ArrayList<>();
    public final QMUISwipeAction mDeleteAction;
    private BaseRecyclerAdapter.OnItemClickListener mClickListener;

    public TemplateAdapter(Context context) {
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textSize(QMUIDisplayHelper.sp2px(context, 14))
                .textColor(Color.WHITE)
                .paddingStartEnd(QMUIDisplayHelper.dp2px(context, 20));

        mDeleteAction = builder.text("删除").backgroundColor(Color.RED).build();
    }

    public void setData(@Nullable List<TemplateInfo> list) {
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

    public void add(int pos, TemplateInfo item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void prepend(@NonNull List<TemplateInfo> items) {
        mData.addAll(0, items);
        notifyDataSetChanged();
    }

    public void append(@NonNull List<TemplateInfo> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public TemplateInfo getItem(int position){
        return mData.get(position);
    }


    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_template, parent, false);
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
        TemplateInfo templateInfo = mData.get(position);
        TextView tvName = holder.itemView.findViewById(R.id.tv_name);
        tvName.setText(templateInfo.name);
        TextView tvDate = holder.itemView.findViewById(R.id.tv_date);
        tvDate.setText(templateInfo.createDate);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        mClickListener = listener;
    }
}
