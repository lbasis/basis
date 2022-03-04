package com.basis.dialog;

import android.app.Activity;
import android.view.View;

public class WXBuilder extends IBuilder<WXBuilder,WXDialog> {
    private Activity activity;

    public WXBuilder(Activity activity) {
        this.activity = activity;
    }

    @Override
    public WXDialog build() {
        return new WXDialog(activity, this);
    }

    /**
     * 构建DialogFragment
     */
    public WFDialog buildFD() {
        return new WFDialog(activity, this);
    }

    /********************* 封装的工具类 **************************/

    /**
     * @param activity Activity
     * @param message  提示消息 cancel：知道了
     */
    public static void showCancelDialog(Activity activity, String message) {
        new WXBuilder(activity)
                .setEnableTitle(true)
                .setMessage(message)
                .cancelStyle()
                .build()
                .show();
    }

    public static void showSureDialog(Activity activity, String message, View.OnClickListener sureClick) {
        new WXBuilder(activity)
                .setEnableTitle(true)
                .setMessage(message)
                .sureStyle(sureClick)
                .build()
                .show();
    }

    public static void showDeleteDialog(Activity activity, String message, View.OnClickListener sureClick) {
        new WXBuilder(activity)
                .setEnableTitle(false)
                .setMessage(message)
                .deleteStyle(sureClick)
                .build()
                .show();
    }

    public static void showDefaultDialog(Activity activity, String message, View.OnClickListener sureClick) {
        new WXBuilder(activity)
                .setEnableTitle(true)
                .setMessage(message)
                .defaultsStyle(sureClick)
                .build()

                .show();
    }

    public static void showCustomDialog(Activity activity, View custom, View.OnClickListener sureClick) {
        new WXBuilder(activity)
                .setEnableTitle(true)
                .setCustomerView(custom)
                .defaultsStyle(sureClick)
                .build()
                .show();
    }
}