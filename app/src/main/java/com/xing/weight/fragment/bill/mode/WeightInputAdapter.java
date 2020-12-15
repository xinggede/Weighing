package com.xing.weight.fragment.bill.mode;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.xing.weight.R;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.RecyclerViewHolder;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.PoundItemInfo;

import java.util.List;

import androidx.annotation.Nullable;

import static com.xing.weight.bean.PoundItemInfo.PoundType;

public class WeightInputAdapter extends BaseRecyclerAdapter<PoundItemInfo> {

    public WeightInputAdapter(Context ctx, @Nullable List<PoundItemInfo> list) {
        super(ctx, list);
    }

    public void updateGoods(GoodsDetail detail){
        for (int i = 0; i < getData().size(); i++) {
            PoundItemInfo info = getItem(i);
            if(info.type == PoundType.GTYPE){
                info.value = detail.name;
                continue;
            }
            if(info.type == PoundType.PRICE){
                info.value = String.valueOf(detail.pricebuy);
                continue;
            }
            if(info.type == PoundType.REMARKS){
                info.value = detail.remark;
                continue;
            }
        }
        notifyDataSetChanged();
    }

    public void updateCustom(CustomerInfo customerInfo){
        for (int i = 0; i < getData().size(); i++) {
            PoundItemInfo info = getItem(i);
            if(info.type == PoundType.RECEIVENAME){
                info.value = customerInfo.comname;
                continue;
            }
            if(info.type == PoundType.DRIVER){
                info.value = customerInfo.name;
                continue;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        PoundItemInfo poundItemInfo = getItem(position);
        if (poundItemInfo.type == PoundType.GTYPE || poundItemInfo.type == PoundType.RECEIVENAME) {
            return 1;
        }
        if (poundItemInfo.type == PoundType.ADD) {
            return 2;
        }
        return 0;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == 1) {
            return R.layout.list_item_input_weight2;
        }
        if (viewType == 2) {
            return R.layout.list_item_input_weight_button;
        }
        return R.layout.list_item_input_weight1;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PoundItemInfo item) {
        int t = getItemViewType(position);
        if (t == 2) {
            holder.setClickListener(R.id.bt_print, new CusClickListener(position));
        } else if (t == 1){
            holder.setText(R.id.tv_name, item.name);
            holder.setClickListener(R.id.tv_value, new CusClickListener(position));
            TextView tvValue = holder.getTextView(R.id.tv_value);
            if (TextUtils.isEmpty(item.hint)) {
                tvValue.setHint(String.format(mContext.getText(R.string.pls_input).toString(), item.name));
            } else {
                tvValue.setHint(item.hint);
            }
            tvValue.setText(item.value);
        } else {
            holder.setText(R.id.tv_name, item.name);

            EditText etValue = holder.getEditText(R.id.et_value);
            if (item.inputType == 0) {
                etValue.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (item.inputType == 1) {
                etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (item.inputType == 2) {
                etValue.setInputType(InputType.TYPE_CLASS_PHONE);
            } else {
                etValue.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etValue.setMinLines(3);
                etValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
            int index = -1;
            InputFilter[] filters = etValue.getFilters();
            for (int i = 0; i < filters.length; i++) {
                if (filters[i] instanceof InputFilter.LengthFilter) {
                    index = i;
                }
            }

            if (index == -1) {
                InputFilter[] nf = new InputFilter[filters.length + 1];
                nf[nf.length - 1] = new InputFilter.LengthFilter(item.lenght);
                etValue.setFilters(nf);
            } else {
                filters[index] = new InputFilter.LengthFilter(item.lenght);
                etValue.setFilters(filters);
            }

            if (TextUtils.isEmpty(item.hint)) {
                etValue.setHint(String.format(mContext.getText(R.string.pls_input).toString(), item.name));
            } else {
                etValue.setHint(item.hint);
            }

            if (etValue.getTag(R.string.change) != null) {
                etValue.removeTextChangedListener((TextWatcher) etValue.getTag(R.string.change));
            }
            etValue.setText(item.value);
            CusTextChanged cusTextChanged = new CusTextChanged(position);
            etValue.addTextChangedListener(cusTextChanged);
            etValue.setTag(R.string.change, cusTextChanged);
        }

    }

    class CusTextChanged implements TextWatcher {

        private int position;

        public CusTextChanged(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            PoundItemInfo item = getItem(position);
            if (item == null) {
                return;
            }
            item.value = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    class CusClickListener implements View.OnClickListener {

        int position;

        public CusClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onChildClickListener != null) {
                onChildClickListener.onChildClick(v, position);
            }
        }
    }

}
