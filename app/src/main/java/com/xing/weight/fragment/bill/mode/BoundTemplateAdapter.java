package com.xing.weight.fragment.bill.mode;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.xing.weight.R;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.RecyclerViewHolder;
import com.xing.weight.bean.PoundItemInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.xing.weight.bean.PoundItemInfo.PoundType;

public class BoundTemplateAdapter extends BaseRecyclerAdapter<PoundItemInfo> {

    public BoundTemplateAdapter(Context ctx, @Nullable List<PoundItemInfo> list) {
        super(ctx, list);
    }

    public void setModelStyle(String name){
        PoundItemInfo item = getItem(1);
        item.value = name;
        notifyItemChanged(1);
    }

    @Override
    public void setData(@Nullable List<PoundItemInfo> list) {
        super.setData(list);
    }

    public List<PoundItemInfo> getChooseItem(){
        List<PoundItemInfo> list = new ArrayList<>();
        for (PoundItemInfo itemInfo : getData()) {
            if(itemInfo.isChecked){
                list.add(itemInfo);
            }
        }
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        PoundItemInfo poundItemInfo = getItem(position);
        if (poundItemInfo.type == PoundType.ADD) {
            return 1;
        }
        if (poundItemInfo.type == PoundType.MODELSTYLE) {
            return 2;
        }
        return 0;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == 1) {
            return R.layout.list_item_input_weight_button;
        }
        if (viewType == 2) {
            return R.layout.list_item_template_outbound1;
        }
        return R.layout.list_item_template_outbound;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PoundItemInfo item) {
        int t = getItemViewType(position);
        if (t == 1) {
            holder.getButton(R.id.bt_confirm).setText(item.name);
            holder.setClickListener(R.id.bt_confirm, new CusClickListener(position));
        } else if(t == 2){
            holder.setVisibility(R.id.checkbox, View.INVISIBLE);
            holder.getCheckBox(R.id.checkbox).setChecked(true);
            holder.setText(R.id.tv_name, item.name);
            TextView tvModelStyle = holder.getEditText(R.id.tv_model_style);
            tvModelStyle.setHint(item.hint);
            tvModelStyle.setText(item.value);
            holder.setClickListener(R.id.iv_choose_style, new CusClickListener(position));
        } else {
            holder.setVisibility(R.id.checkbox, item.type == PoundType.MODELNAME?View.INVISIBLE: View.VISIBLE);
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

            CheckBox checkBox =  holder.getCheckBox(R.id.checkbox);
            checkBox.setChecked(item.isChecked);
            checkBox.setOnCheckedChangeListener(new CusCheckListener(position));

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

    class CusCheckListener implements CompoundButton.OnCheckedChangeListener {

        int position;

        public CusCheckListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PoundItemInfo item = getItem(position);
            if (item == null) {
                return;
            }
            item.isChecked = isChecked;
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
