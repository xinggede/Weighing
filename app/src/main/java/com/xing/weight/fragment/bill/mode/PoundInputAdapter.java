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

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.Nullable;

import static com.xing.weight.bean.PoundItemInfo.PoundType;

public class PoundInputAdapter extends BaseRecyclerAdapter<PoundItemInfo> {

    private int realWeight = -1, carWeight = -1, totalWeight, discount = -1, price = -1, totalPrice = -1, inTime = -1, outTime = -1;

    public PoundInputAdapter(Context ctx, @Nullable List<PoundItemInfo> list) {
        super(ctx, list);
    }

    @Override
    public void setData(@Nullable List<PoundItemInfo> list) {
        super.setData(list);
        for (int i = 0; i < list.size(); i++) {
            PoundItemInfo info = getItem(i);
            if (info.type == PoundType.CARWEIGHT) {
                carWeight = i;
                continue;
            }
            if (info.type == PoundType.TOTALWEIGHT) {
                totalWeight = i;
                continue;
            }
            if (info.type == PoundType.REALWEIGHT) {
                realWeight = i;
                continue;
            }
            if (info.type == PoundType.DISCOUNT) {
                discount = i;
                continue;
            }
            if (info.type == PoundType.TOTALPRICE) {
                totalPrice = i;
                continue;
            }
            if (info.type == PoundType.PRICE) {
                price = i;
                continue;
            }
            if (info.type == PoundType.INTIME) {
                inTime = i;
                continue;
            }
            if (info.type == PoundType.OUTTIME) {
                outTime = i;
                continue;
            }
        }
    }

    public void updateWeight() {
        if (realWeight == -1 || totalWeight == -1 || carWeight == -1 || discount == -1) {
            return;
        }
        String tw = getItem(totalWeight).value;
        if (TextUtils.isEmpty(tw)) {
            tw = "0";
        }
        String cw = getItem(carWeight).value;
        if (TextUtils.isEmpty(cw)) {
            cw = "0";
        }
        String dis = getItem(discount).value;
        if (TextUtils.isEmpty(dis)) {
            dis = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(tw);
        BigDecimal result = bigDecimal.subtract(new BigDecimal(cw)).subtract(bigDecimal.multiply(new BigDecimal(dis)))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        String value = result.toString();
        getItem(realWeight).value = value;
        notifyItemChanged(realWeight);

        updatePrice();
    }

    public void updatePrice() {
        if (realWeight == -1 || totalPrice == -1 || price == -1) {
            return;
        }
        String tw = getItem(realWeight).value;
        String p = getItem(price).value;
        if (TextUtils.isEmpty(p)) {
            p = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(tw);
        BigDecimal total = bigDecimal.multiply(new BigDecimal(p)).setScale(2, BigDecimal.ROUND_HALF_UP);
        String t = total.toString();
        getItem(totalPrice).value = t;
        notifyItemChanged(totalPrice);
    }

    //净重=毛重-皮重-毛重*折损
    //总价=净重*单价

    public void updateGoods(GoodsDetail detail) {
        for (int i = 0; i < getData().size(); i++) {
            PoundItemInfo info = getItem(i);
            if (info.type == PoundType.GTYPE) {
                info.value = detail.name;
                continue;
            }
            if (info.type == PoundType.PRICE) {
                info.value = String.valueOf(detail.pricebuy);
                continue;
            }
            if (info.type == PoundType.REMARKS) {
                info.value = detail.remark;
                continue;
            }
        }
        notifyDataSetChanged();
    }

    public void updateCustom(CustomerInfo customerInfo) {
        for (int i = 0; i < getData().size(); i++) {
            PoundItemInfo info = getItem(i);
            if (info.type == PoundType.RECEIVENAME) {
                info.value = customerInfo.comname;
                continue;
            }
            if (info.type == PoundType.DRIVER) {
                info.value = customerInfo.name;
                continue;
            }
        }
        notifyDataSetChanged();
    }

    public void updateTime(PoundType type, String value){
        if(type == PoundType.INTIME){
            getItem(inTime).value = value;
            notifyItemChanged(inTime);
        } else {
            getItem(outTime).value = value;
            notifyItemChanged(outTime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        PoundItemInfo poundItemInfo = getItem(position);
        if (poundItemInfo.type == PoundType.GTYPE || poundItemInfo.type == PoundType.RECEIVENAME
                || poundItemInfo.type == PoundType.INTIME || poundItemInfo.type == PoundType.OUTTIME) {
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
            holder.setClickListener(R.id.bt_confirm, new CusClickListener(position));
        } else if (t == 1) {
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
            if (item.type == PoundType.CARWEIGHT || item.type == PoundType.TOTALWEIGHT || item.type == PoundType.DISCOUNT) {
                updateWeight();
            }
            if (item.type == PoundType.REALWEIGHT || item.type == PoundType.PRICE) {
                updatePrice();
            }
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
