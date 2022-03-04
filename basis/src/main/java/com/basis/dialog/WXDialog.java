package com.basis.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.basis.R;
import com.basis.percent.PercentLinearLayout;
import com.kit.utils.Logger;
import com.kit.utils.ResUtil;

/**
 * @author: BaiCQ
 * @createTime: 2016/12/22 18:04
 * @className: WXDialog
 * @Description: 最新标准微信风格弹框
 */
public class WXDialog implements IDialog {
    private BasisDialog basisDialog;
    private TextView title;//标题
    private PercentLinearLayout llContent;//文本内容和添加view的父布局
    private TextView message;//内容
    private TextView confirm, cancel;//按钮

    private IBuilder builder;

    /**
     * 默认布局构建
     *
     * @param activity
     */
    protected WXDialog(Activity activity, IBuilder builder) {
        basisDialog = new BasisDialog(activity, R.style.Basis_Style_WX_Dialog, Gravity.CENTER)
                .setContentView(R.layout.basis_layout_wx, 74, -1);
        initView();
        builder.callback = new IBuilder.OnRefreshCallback() {
            @Override
            public void onRefresh() {
                handleRefresh();
            }
        };
        this.builder = builder;
    }

    private void initView() {
        confirm = basisDialog.getView(R.id.btn_confirm);
        cancel = basisDialog.getView(R.id.btn_cancel);
        llContent = basisDialog.getView(R.id.ll_content);
        message = basisDialog.getView(R.id.tv_message);
        title = basisDialog.getView(R.id.tv_title);
    }

    void handleRefresh() {
        Logger.e("WXDialog", "handleRefresh");
        // title
        title.setVisibility(builder.enableTitle ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(builder.title)) {
            title.setText(builder.title);
        }
        // message
        message.setText(builder.message);
        // cancel
        cancel.setVisibility(builder.enableCancel ? View.VISIBLE : View.GONE);
        if (builder.cancelColor > 0) cancel.setTextColor(ResUtil.getColor(builder.cancelColor));
        if (!TextUtils.isEmpty(builder.cancelText)) cancel.setText(builder.cancelText);
        if (builder.cancelBg > 0) cancel.setBackground(ResUtil.getDrawable(builder.cancelBg));
        if (null == builder.cancelClick) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            cancel.setOnClickListener(builder.cancelClick);
        }
        //sure
        confirm.setVisibility(builder.enableSure ? View.VISIBLE : View.GONE);
        if (builder.sureColor > 0) confirm.setTextColor(ResUtil.getColor(builder.sureColor));
        if (!TextUtils.isEmpty(builder.sureText)) confirm.setText(builder.sureText);
        if (builder.sureBg > 0) confirm.setBackground(ResUtil.getDrawable(builder.sureBg));
        confirm.setOnClickListener(builder.sureClick);
        if (null == builder.sureClick) {
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            confirm.setOnClickListener(builder.sureClick);
        }
        //out cancel
        basisDialog.setCanceledOnTouchOutside(builder.outsideCanceled);
        // customer
        if (null != builder.customerView) {
            llContent.removeAllViews();
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            llContent.addView(builder.customerView, params);
        }
    }

    @Override
    public Dialog getDialog() {
        return basisDialog;
    }

    @Override
    public WXDialog setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        basisDialog.observeDismiss(dismissListener);
        return this;
    }

    @Override
    public void dismiss() {
        if (basisDialog != null) {
            basisDialog.dismiss();
            basisDialog = null;
        }
    }

    @Override
    public void show() {
        if (null != basisDialog && !basisDialog.isShowing()) {
            if (null != builder) builder.refresh();
            basisDialog.show();
        }
    }
}
