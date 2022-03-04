package com.basis.dialog;

import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.basis.R;
import com.kit.utils.ResUtil;

public abstract class IBuilder<T extends IBuilder,D extends IDialog> {
    protected boolean outsideCanceled = true;
    // title
    protected String title = "提示";
    protected boolean enableTitle = false;
    // message
    protected CharSequence message = "";
    protected boolean enableCancel = true;
    // cancel
    protected String cancelText = "取消";
    protected @ColorRes
    int cancelColor = -1;
    protected @DrawableRes
    int cancelBg = -1;
    protected View.OnClickListener cancelClick;
    // sure
    protected boolean enableSure = true;
    protected String sureText = "确定";
    protected @ColorRes
    int sureColor = -1;
    protected @DrawableRes
    int sureBg = -1;
    protected View.OnClickListener sureClick;
    // callback
    protected OnRefreshCallback callback;
    // 自定义view
    protected View customerView;

    public IBuilder(){

    }
    public T setOutsideCanceled(boolean outsideCanceled) {
        this.outsideCanceled = outsideCanceled;
        return (T) this;
    }

    public T setEnableTitle(boolean enableTitle) {
        this.enableTitle = enableTitle;
        return (T) this;
    }

    public T setTitle(String title) {
        this.title = title;
        return (T) this;
    }

    public T setMessage(CharSequence message) {
        this.message = message;
        return (T) this;
    }

    public T setEnableCancel(boolean enableCancel) {
        this.enableCancel = enableCancel;
        return (T) this;
    }

    public T setCancelBtnStyle(String cancelText, @ColorRes int cancelColor, @DrawableRes int cancelBg, View.OnClickListener cancelClick) {
        this.cancelText = cancelText;
        this.cancelColor = cancelColor;
        this.cancelBg = cancelBg;
        this.cancelClick = cancelClick;
        this.enableCancel = true;
        return (T) this;
    }

    public T setSureBtnStyle(String sureText, @ColorRes int sureColor, @DrawableRes int sureBg, View.OnClickListener sureClick) {
        this.sureColor = sureColor;
        this.sureText = sureText;
        this.sureBg = sureBg;
        this.sureClick = sureClick;
        this.enableSure = true;
        return (T) this;
    }

    public T setEnableSure(boolean enableSure) {
        this.enableSure = enableSure;
        return (T) this;
    }

    public T setCustomerView(View customerView) {
        this.customerView = customerView;
        return (T) this;
    }

    public void refresh() {
        if (null != callback) callback.onRefresh();
    }

    public abstract D build();

    public interface OnRefreshCallback {
        void onRefresh();
    }

    public T defaultsStyle(View.OnClickListener sureClick) {
        return (T) setCancelBtnStyle(ResUtil.getString(R.string.basis_cancle), -1, -1, null)
                .setSureBtnStyle(ResUtil.getString(R.string.basis_ok), -1, -1, sureClick);
    }

    public T cancelStyle() {
        return (T) setCancelBtnStyle(ResUtil.getString(R.string.basis_cancle), -1, -1, null)
                .setEnableSure(false);

    }

    public T sureStyle(View.OnClickListener sureClick) {
        return (T) setEnableCancel(false)
                .setSureBtnStyle(ResUtil.getString(R.string.basis_ok), -1, -1, sureClick);
    }

    public T deleteStyle(View.OnClickListener sureClick) {
        return (T) setCancelBtnStyle(ResUtil.getString(R.string.basis_cancle), -1, -1, null)
                .setSureBtnStyle(ResUtil.getString(R.string.basis_ok), android.R.color.white, R.drawable.selector_red_solid, sureClick);
    }
}
