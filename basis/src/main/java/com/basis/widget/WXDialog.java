package com.basis.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.basis.R;
import com.basis.percent.PercentLinearLayout;
import com.basis.widget.interfaces.IDialog;
import com.kit.utils.ResUtil;

/**
 * @author: BaiCQ
 * @createTime: 2016/12/22 18:04
 * @className: WXDialog
 * @Description: 最新标准微信风格弹框
 */
public class WXDialog implements IDialog<WXDialog> {
    private BasisDialog dialog;
    private TextView title;//标题
    private PercentLinearLayout llContent;//文本内容和添加view的父布局
    private TextView message;//内容
    private TextView confirm, cancel;//按钮
    private DialogInterface.OnDismissListener dismissListener;

    public interface CustomBuilder {
        View onBuild();
    }

    public WXDialog() {
    }

    /**
     * 默认布局构建
     *
     * @param activity
     */
    public WXDialog(final Activity activity) {
        dialog = new BasisDialog(activity, R.style.Basis_Style_WX_Dialog, Gravity.CENTER)
                .setContentView(R.layout.basis_layout_wx, 74, -1);
        initView();
    }

    private void initView() {
        confirm = dialog.getView(R.id.btn_confirm);
        cancel = dialog.getView(R.id.btn_cancel);
        llContent = dialog.getView(R.id.ll_content);
        message = dialog.getView(R.id.tv_message);
        title = dialog.getView(R.id.tv_title);
        title.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
    }

    @Override
    public WXDialog setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

    @Override
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void show() {
        if (null != dialog && !dialog.isShowing()) dialog.show();
    }

    @Override
    public WXDialog setOutsideCanceled(boolean outsideCancele) {
        if (null != dialog) dialog.setCanceledOnTouchOutside(outsideCancele);
        return this;
    }

    @Override
    public WXDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(title);
        }
        return this;
    }


    @Override
    public WXDialog setMessage(CharSequence message) {
        if (!TextUtils.isEmpty(message)) this.message.setText(message);
        return this;
    }


    @Override
    public WXDialog setSureButton(String text, View.OnClickListener onclick) {
        return setSureButton(text, -1, -1, onclick, true);
    }

    @Override
    public WXDialog setSureButton(String text, // 按钮文本
                                  @ColorRes int color, // 文本颜色
                                  @DrawableRes int btnBg, // 按钮背景
                                  final View.OnClickListener onclick) {
        return setSureButton(text, color, btnBg, onclick, true);
    }

    @SuppressLint("ResourceType")
    @Override
    public WXDialog setSureButton(String text, // 按钮文本
                                  @ColorRes int color, // 文本颜色
                                  @DrawableRes int btnBg, // 按钮背景
                                  final View.OnClickListener onclick,
                                  boolean clickDismiss) { // 点击是否消失 处理一些特殊需求 点击确定按钮后弹框不消失
        confirm.setVisibility(View.VISIBLE);
        if (color > 0) confirm.setTextColor(ResUtil.getColor(color));
        if (!TextUtils.isEmpty(text)) confirm.setText(text);
        if (btnBg > 0) confirm.setBackground(ResUtil.getDrawable(btnBg));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onclick) onclick.onClick(v);
                if (clickDismiss) dismiss();
            }
        });
        return this;
    }

    @Override
    public WXDialog setCancelButton(String text) {
        return setCancelButton(text, -1, -1);
    }

    @SuppressLint("ResourceType")
    @Override
    public WXDialog setCancelButton(String text, // 按钮文本
                                    @ColorInt int color, // 文本颜色
                                    @DrawableRes int btnBg) {// 按钮背景
        cancel.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) cancel.setText(text);
        if (color > 0) cancel.setTextColor(ResUtil.getColor(color));
        if (btnBg > 0) cancel.setBackground(ResUtil.getDrawable(btnBg));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return this;
    }

    @Override
    public WXDialog addCustomContentView(View customContentView) {
        if (null != llContent && null != customContentView) {
            llContent.removeAllViews();
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            llContent.addView(customContentView, params);
        }
        return this;
    }

    @Override
    public WXDialog hideCancelButton() {
        cancel.setVisibility(View.GONE);
        return this;
    }

    @Override
    public WXDialog hideSureButton() {
        confirm.setVisibility(View.GONE);
        return this;
    }

    @Override
    public WXDialog defalutStyle(boolean title, View.OnClickListener sureClick) {
        return title ? setTitle(ResUtil.getString(R.string.basis_tip))
                .setCancelButton(ResUtil.getString(R.string.basis_cancle))
                .setSureButton(ResUtil.getString(R.string.basis_ok), sureClick)
                : setCancelButton(ResUtil.getString(R.string.basis_cancle))
                .setSureButton(ResUtil.getString(R.string.basis_ok), sureClick);
    }

    @Override
    public WXDialog cancelStyle(boolean title) {
        return title ? setTitle(ResUtil.getString(R.string.basis_tip))
                .setCancelButton(ResUtil.getString(R.string.basis_cancle))
                : setCancelButton(ResUtil.getString(R.string.basis_cancle));
    }

    @Override
    public WXDialog sureStyle(boolean title, View.OnClickListener sureClick) {
        return title ? setTitle(ResUtil.getString(R.string.basis_tip))
                .setSureButton(ResUtil.getString(R.string.basis_ok), sureClick)
                : setSureButton(ResUtil.getString(R.string.basis_ok), sureClick);
    }

    @Override
    public WXDialog deleteStyle(boolean title, View.OnClickListener sureClick) {
        return title ? setTitle(ResUtil.getString(R.string.basis_tip))
                .setCancelButton(ResUtil.getString(R.string.basis_cancle))
                .setSureButton(ResUtil.getString(R.string.basis_delete),
                        android.R.color.white,
                        R.drawable.selector_red_solid,
                        sureClick)
                : setCancelButton(ResUtil.getString(R.string.basis_cancle))
                .setSureButton(ResUtil.getString(R.string.basis_delete),
                        android.R.color.white,
                        R.drawable.selector_red_solid,
                        sureClick);
    }

    /********************* 封装的工具类 **************************/

    /**
     * @param activity Activity
     * @param message  提示消息 cancel：知道了
     */
    public static void showCancelDialog(Activity activity, String message) {
        new WXDialog(activity)
                .setMessage(message)
                .cancelStyle(true)
                .show();
    }

    public static void showSureDialog(Activity activity, String message, View.OnClickListener sureClick) {
        new WXDialog(activity)
                .setMessage(message)
                .sureStyle(true, sureClick)
                .show();
    }

    public static void showDeleteDialog(Activity activity, String message, View.OnClickListener sureClick) {
        new WXDialog(activity)
                .setMessage(message)
                .deleteStyle(false, sureClick)
                .show();
    }

    public static void showDefaultDialog(Activity activity, String message, View.OnClickListener sureClick) {
        new WXDialog(activity)
                .setMessage(message)
                .defalutStyle(true, sureClick)
                .show();
    }

    public static void showCustomDialog(Activity activity, View custom, View.OnClickListener sureClick) {
        new WXDialog(activity)
                .defalutStyle(true, sureClick)
                .addCustomContentView(custom)
                .show();
    }
}
