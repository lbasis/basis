package com.basis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public interface IDialog {

    /**
     * 设置消失监听
     *
     * @param dismissListener 消失监听
     */
    IDialog setOnDismissListener(DialogInterface.OnDismissListener dismissListener);

    void dismiss();


    void show();

    Dialog getDialog();

}
