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
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.util.KeyBoardUtil;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.Nullable;

import static com.xing.weight.bean.PoundItemInfo.PoundType;

public class BoundInputAdapter extends BaseRecyclerAdapter<PoundItemInfo> {

    private int rName = -1, rContacts = -1, rAddress, rPhone = -1, outTime = -1;

    public BoundInputAdapter(Context ctx, @Nullable List<PoundItemInfo> list) {
        super(ctx, list);
    }

    public void reset() {
        rName = -1;
        rContacts = -1;
        rAddress = -1;
        rPhone = -1;
        outTime = -1;
    }

    @Override
    public void setData(@Nullable List<PoundItemInfo> list) {
        super.setData(list);
        reset();
        for (int i = 0; i < list.size(); i++) {
            PoundItemInfo info = getItem(i);
            if (info.type == PoundType.RECEIVENAME) {
                rName = i;
                continue;
            }
            if (info.type == PoundType.RECEIVECONTACTS) {
                rContacts = i;
                continue;
            }
            if (info.type == PoundType.RECEIVEADDRESS) {
                rAddress = i;
                continue;
            }
            if (info.type == PoundType.RECEIVEPHONE) {
                rPhone = i;
                continue;
            }
            if (info.type == PoundType.OUTTIME) {
                outTime = i;
                continue;
            }
        }
    }

    public void updateCustom(CustomerInfo customerInfo) {
        if(rName != -1){
            getItem(rName).value = customerInfo.comname;
            notifyItemChanged(rName);
        }
        if(rContacts != -1){
            getItem(rContacts).value = customerInfo.name;
            notifyItemChanged(rContacts);
        }
        if(rAddress != -1){
            getItem(rAddress).value = customerInfo.address;
            notifyItemChanged(rAddress);
        }
        if(rPhone != -1){
            getItem(rPhone).value = customerInfo.phone;
            notifyItemChanged(rPhone);
        }
        notifyDataSetChanged();
    }

    public void updateTime(PoundType type, String value) {
        if(outTime != -1){
            getItem(outTime).value = value;
            notifyItemChanged(outTime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        PoundItemInfo poundItemInfo = getItem(position);
        if (poundItemInfo.type == PoundType.RECEIVENAME || poundItemInfo.type == PoundType.OUTTIME) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == 1) {
            return R.layout.list_item_input_weight2;
        }
        return R.layout.list_item_input_weight1;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PoundItemInfo item) {
        int t = getItemViewType(position);
        if (t == 1) {
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
            etValue.setMinLines(0);
            etValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            if (item.inputType == 0) {
                etValue.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (item.inputType == 1) {
                etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (item.inputType == 2) {
                etValue.setInputType(InputType.TYPE_CLASS_PHONE);
            } else {
                etValue.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
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
