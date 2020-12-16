package com.xing.weight.fragment.print;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.fragment.main.my.PrintAddFragment;
import com.xing.weight.fragment.print.mode.PrintContract;
import com.xing.weight.fragment.print.mode.PrintPresenter;
import com.xing.weight.util.Tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class PrintPreviewFragment extends BaseFragment<PrintPresenter> implements PrintContract.View {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.iv_preview)
    ImageView ivPreview;
    @BindView(R.id.tv_print)
    TextView tvPrint;
    @BindView(R.id.tv_print_type)
    TextView tvPrintType;
    private PrinterInfo printerInfo;
    private QMUIPopup choosePrint, choosePrintType;
    private int printType = 0;
    private PoundInfo info;

    public PrintPreviewFragment(PoundInfo poundInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, poundInfo);
        setArguments(bundle);
    }

    @Override
    protected PrintPresenter onLoadPresenter() {
        return new PrintPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print_preview;
    }

    @Override
    protected void initView(View view) {
        info = getArguments().getParcelable(Constants.DATA);

        topbar.setTitle("打印预览");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        if (info != null) {
            showLoading();
            Glide.with(this).asBitmap().load(info.url).placeholder(R.mipmap.img_default).error(R.mipmap.img_default)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ivPreview.setImageBitmap(resource);
                            hideLoading();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            ivPreview.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            ivPreview.setImageDrawable(errorDrawable);
                            hideLoading();
                        }
                    });
        }

        mPresenter.getPrints(false);
    }

    @OnClick({R.id.tv_print, R.id.tv_print_type, R.id.bt_print, R.id.ib_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_print:
                if (!mPresenter.getSavePrints().isEmpty()) {
                    showChoosePrint(view);
                } else {
                    mPresenter.getPrints(true);
                }
                break;

            case R.id.tv_print_type:
                showChoosePrintType(view);
                break;

            case R.id.bt_print:

                break;

            default:
                callback();
                startFragment(new PrintAddFragment(null));
                break;
        }
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(PrintAddFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {
                boolean value = (boolean) effect.getValue(PrintAddFragment.class.getName());
                if (value) {
                    mPresenter.getPrints(false);
                }
            }
        });
    }

    private void showChoosePrint(View v) {
        if (choosePrint == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSavePrints());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (choosePrint != null) {
                        choosePrint.dismiss();
                    }
                    printerInfo = (PrinterInfo) adapterView.getItemAtPosition(i);
                    tvPrint.setText(printerInfo.name + ":" + printerInfo.devcode);
                }
            };
            choosePrint = QMUIPopups.listPopup(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 200),
                    QMUIDisplayHelper.dp2px(getContext(), 300),
                    adapter,
                    onItemClickListener)
                    .bgColor(ContextCompat.getColor(getContext(), R.color.tab_bj))
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_TOP)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        choosePrint.show(v);
    }

    private void showChoosePrintType(View v) {
        if (choosePrintType == null) {
            String[] data = new String[]{"云打印", "蓝牙打印"};
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, data);
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (choosePrintType != null) {
                        choosePrintType.dismiss();
                    }
                    printType = i;
                    tvPrintType.setText(adapterView.getItemAtPosition(i).toString());
                }
            };
            choosePrintType = QMUIPopups.listPopup(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 200),
                    QMUIDisplayHelper.dp2px(getContext(), 300),
                    adapter,
                    onItemClickListener)
                    .bgColor(ContextCompat.getColor(getContext(), R.color.tab_bj))
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_TOP)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        choosePrintType.show(v);
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (success) {
            if (code == 0) {
                if (!mPresenter.getSavePrints().isEmpty()) {
                    printerInfo = mPresenter.getSavePrints().get(0);
                    tvPrint.setText(printerInfo.name + ":" + printerInfo.devcode);
                }
            } else if (code == 1) {
                if (!mPresenter.getSavePrints().isEmpty()) {
                    showChoosePrint(tvPrint);
                } else {
                    tvPrint.setText("请先添加一个打印机");
                }
            }
        }
    }
}
