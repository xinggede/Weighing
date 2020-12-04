package com.xing.weight.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 星哥的 on 2018/4/11.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int space;
    int type;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, int type) {
        this.space = space;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (type == 0) {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = space;
            }
        } else if (type == 1) { //my document
            int index = parent.getChildAdapterPosition(view);
            if (index != 0 && index != 1) {
                outRect.top = space;
            }
            if (index % 2 != 0) {
                outRect.left = space;
            }
        } else if (type == 2) { //files list
            int index = parent.getChildAdapterPosition(view);
            if (index != 0) {
                outRect.top = space;
            }
        } else if (type == 3) { //files grid
            int index = parent.getChildAdapterPosition(view);
            if (index != 0 && index != 1 && index != 2) {
                outRect.top = space;
            }
            if (index % 3 != 0) {
                outRect.left = space;
            }
        } else if (type == 4) { //images grid
            int index = parent.getChildAdapterPosition(view);
            if (index > 3) {
                outRect.top = space;
            }
            if (index % 4 != 0) {
                outRect.left = space;
            }
        }else if (type == 5) { //lables grid
            int index = parent.getChildAdapterPosition(view);
            if (index != 0 && index != 1) {
                outRect.top = space;
            }
            if (index % 2 != 0) {
                outRect.left = space;
            }
        }

    }
}
