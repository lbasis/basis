package com.basis.widget.interfaces;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public interface IDialog<T extends IDialog> {

    /**
     * 设置消失监听
     *
     * @param dismissListener 消失监听
     */
    T setOnDismissListener(DialogInterface.OnDismissListener dismissListener);

    void dismiss();


    void show();

    /**
     * 设置点击外部区域取消弹框
     *
     * @param outsideCancele
     */
    T setOutsideCanceled(boolean outsideCancele);

    /**
     * @param title 设置标题
     */
    T setTitle(String title);

    /**
     * 设置信息
     *
     * @param message
     */
    T setMessage(CharSequence message);

    /**
     * @param text
     * @param onclick
     */
    T setSureButton(String text, View.OnClickListener onclick);

    /**
     * 设置确定按钮
     *
     * @param text      按钮文本
     * @param textColor 文本颜色
     * @param btnBg     按钮背景
     * @param onclick   点击事件
     */
    T setSureButton(String text, @ColorRes int textColor, @DrawableRes int btnBg, View.OnClickListener onclick);

    /**
     * 设置确定按钮
     *
     * @param text         按钮文本
     * @param textColor    文本颜色
     * @param btnBg        按钮背景¬
     * @param onclick      点击事件
     * @param clickDismiss 点击是否消失 处理一些特殊需求 点击确定按钮后弹框不消失
     */
    T setSureButton(String text, @ColorRes int textColor, @DrawableRes int btnBg, View.OnClickListener onclick, boolean clickDismiss);

    /**
     * 设置取消按钮
     *
     * @param text 按钮文本
     */
    T setCancelButton(String text);

    /**
     * 设置取消按钮
     *
     * @param text      按钮文本
     * @param textColor 文本颜色
     * @param btnBg     按钮背景
     */
    T setCancelButton(String text, @ColorRes int textColor, @DrawableRes int btnBg);

    /**
     * 添加自定义Content视图组件
     *
     * @param customView
     */
    T addCustomContentView(View customView);

    /**
     * 隐藏取消按钮
     */
    T hideCancelButton();

    /**
     * 隐藏确定按钮
     */
    T hideSureButton();

    /**
     * 默认风格弹框
     *
     * @param title     是否显示默认标题
     * @param sureClick 确定按钮事件
     */
    T defalutStyle(boolean title, View.OnClickListener sureClick);

    /**
     * 取消风格弹框
     *
     * @param title 是否显示默认标题
     */
    T cancelStyle(boolean title);

    /**
     * 确认风格弹框
     *
     * @param title     是否显示默认标题
     * @param sureClick 确定按钮事件
     */
    T sureStyle(boolean title, View.OnClickListener sureClick);

    /**
     * 删除风格弹框
     *
     * @param title     是否显示默认标题
     * @param sureClick 确定按钮事件
     */
    T deleteStyle(boolean title, View.OnClickListener sureClick);
}
