package com.xing.weight.fragment.bill.mode;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


import com.xing.weight.R;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.RecyclerViewHolder;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.util.Tools;
import com.xing.weight.view.CusFloatEditText;

import java.util.List;

import androidx.annotation.Nullable;

public class GoodsAdapter extends BaseRecyclerAdapter<GoodsDetail> {

    private InputChangeListener inputChangeListener;

    public GoodsAdapter(Context ctx, @Nullable List<GoodsDetail> list) {
        super(ctx, list);
    }

    public void setInputChangeListener(InputChangeListener inputChangeListener) {
        this.inputChangeListener = inputChangeListener;
    }

    public void removeCusTextChanged(int position) {
        notifyDataSetChanged();
    }

    public void removeCusTextChangedAll() {
    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.list_item_bound_goods;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, GoodsDetail item) {
        holder.getView(R.id.tv_name).setFocusableInTouchMode(true);
        holder.getView(R.id.tv_name).setFocusable(true);
        holder.setText(R.id.tv_name, item.name);

        CusFloatEditText etPrice = (CusFloatEditText) holder.getView(R.id.et_price);
        if (etPrice.getTag(R.string.change) != null) {
            etPrice.removeTextChangedListener((TextWatcher) etPrice.getTag(R.string.change));
        }
        etPrice.setText(Tools.doubleToString2(item.price));

        EditText etNumber = (EditText) holder.getView(R.id.et_number);
        if (etNumber.getTag(R.string.change) != null) {
            etNumber.removeTextChangedListener((TextWatcher) etNumber.getTag(R.string.change));
        }
        etNumber.setText(String.valueOf(item.count));

        CusFloatEditText etAmount = (CusFloatEditText) holder.getView(R.id.et_amount);
        etAmount.setText(Tools.doubleToString2(item.money));

        CusPriceTextChanged priceTextChanged = new CusPriceTextChanged(position, etPrice, etAmount);
        etPrice.addTextChangedListener(priceTextChanged);
        etPrice.setTag(R.string.change, priceTextChanged);

        CusNumberTextChanged numberTextChanged = new CusNumberTextChanged(position, etNumber, etAmount);
        etNumber.addTextChangedListener(numberTextChanged);
        etNumber.setTag(R.string.change, numberTextChanged);

        holder.setClickListener(R.id.bt_jia, new CusClick(position, etPrice, etNumber, etAmount));
        holder.setClickListener(R.id.bt_jian, new CusClick(position, etPrice, etNumber, etAmount));
        holder.setClickListener(R.id.ib_more, new CusClick(position, etPrice, etNumber, etAmount));

        CusFocus cusFocus;
        if (etPrice.getTag(R.string.focus) != null) {
            cusFocus = (CusFocus) etPrice.getTag(R.string.focus);
        } else {
            cusFocus = new CusFocus(position, etPrice, etNumber, etAmount);
        }
        etPrice.setOnFocusChangeListener(cusFocus);
        etNumber.setOnFocusChangeListener(cusFocus);

        etPrice.setTag(R.string.focus, cusFocus);

    }

    class CusFocus implements View.OnFocusChangeListener {

        private int position;
        private EditText etPrice, etNumber, etAmount;

        public CusFocus(int position, EditText etPrice, EditText etNumber, EditText etAmount) {
            this.position = position;
            this.etPrice = etPrice;
            this.etNumber = etNumber;
            this.etAmount = etAmount;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            GoodsDetail item = getItem(position);
            if (item == null) {
                return;
            }
            if (hasFocus) {
                if (v.getId() == R.id.et_price) {
                    etPrice.setText("");
                } else if (v.getId() == R.id.et_number) {
                    etNumber.setText("");
                } else {
                }
            }
        }
    }

    class CusClick implements View.OnClickListener {

        private int position;
        private EditText etPrice, etNumber, etAmount;

        public CusClick(int position, EditText etPrice, EditText etNumber, EditText etAmount) {
            this.position = position;
            this.etPrice = etPrice;
            this.etNumber = etNumber;
            this.etAmount = etAmount;
        }

        @Override
        public void onClick(View v) {
            GoodsDetail item = getItem(position);
            if (item == null) {
                return;
            }
            int number = Tools.parseInt(etNumber.getText().toString());
            if (v.getId() == R.id.bt_jia) {
                number = number + 1;
            } else {
                if (number >= 1) {
                    number = number - 1;
                } else {
                    return;
                }
            }
            etPrice.clearFocus();
            etNumber.clearFocus();
            etAmount.clearFocus();

            item.count = number;
            item.money = Tools.stringToDouble2(item.getCalcAmount());
            etNumber.setText(Tools.doubleToString3(number));
            etAmount.setText(Tools.doubleToString2(item.money));

            if (inputChangeListener != null) {
                inputChangeListener.onChange(String.valueOf(number));
            }
        }
    }


    class CusPriceTextChanged implements TextWatcher {

        private int position;
        private EditText etPrice, etAmount;

        public CusPriceTextChanged(int position, EditText etPrice, EditText etAmount) {
            this.position = position;
            this.etPrice = etPrice;
            this.etAmount = etAmount;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            GoodsDetail item = getItem(position);
            if (item == null) {
                return;
            }
            double price = Tools.stringToDouble2(s.toString());
            if (price > 9999999.99) {
                price = 9999999.99;
                etPrice.setText("9999999.99");
                etPrice.setSelection(etPrice.length());
            }
            item.price = price;
            item.money = Tools.stringToDouble2(item.getCalcAmount());

            etAmount.setText(Tools.doubleToString2(item.money));

            if (inputChangeListener != null) {
                inputChangeListener.onChange(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    class CusNumberTextChanged implements TextWatcher {

        private int position;
        private EditText etNumber, etAmount;

        public CusNumberTextChanged(int position, EditText etNumber, EditText etAmount) {
            this.position = position;
            this.etNumber = etNumber;
            this.etAmount = etAmount;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            GoodsDetail item = getItem(position);
            if (item == null) {
                return;
            }

            int number = Tools.parseInt(s.toString());
            if (number > 999999) {
                number = 999999;
                etNumber.setText("999999");
                etNumber.setSelection(etNumber.length());
            }
            item.count = number;
            item.money = Tools.stringToDouble2(item.getCalcAmount());
            etAmount.setText(Tools.doubleToString2(item.money));

            if (inputChangeListener != null) {
                inputChangeListener.onChange(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
