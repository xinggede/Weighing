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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyCustomAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder> {

    private List<String> mData = new ArrayList<>();
    public final QMUISwipeAction mDeleteAction;
    private BaseRecyclerAdapter.OnItemClickListener mClickListener;

    public MyCustomAdapter(Context context) {
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textSize(QMUIDisplayHelper.sp2px(context, 14))
                .textColor(Color.WHITE)
                .paddingStartEnd(QMUIDisplayHelper.dp2px(context, 14));

        mDeleteAction = builder.text("删除").backgroundColor(Color.RED).build();
    }

    public void setData(@Nullable List<String> list) {
        mData.clear();
        if (list != null) {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void add(int pos, String item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void prepend(@NonNull List<String> items) {
        mData.addAll(0, items);
        notifyDataSetChanged();
    }

    public void append(@NonNull List<String> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_custom, parent, false);
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
        TextView textView = holder.itemView.findViewById(R.id.tv_company_name);
        textView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        mClickListener = listener;
    }
}
