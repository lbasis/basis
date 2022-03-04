package com.basis.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.basis.R;
import com.kit.utils.Logger;
import com.kit.utils.ScreenUtil;

/**
 * @author: BaiCQ
 * @createTime: 2016/12/22 18:04
 * @className: BasisDialog
 * @Description: Gravity.CENTER, Gravity.BOTTOM控制底部弹框
 */
public class BasisDialog extends Dialog {
    protected final String TAG = getClass().getSimpleName();
    private int gravity = Gravity.CENTER;
    private View contentView;
    private OnDismissListener listener;

    public BasisDialog(Activity activity, int gravity) {
        this(activity, -1, gravity);
    }

    public BasisDialog(Activity activity, int style, int gravity) {
        super(activity, style < 0 ? R.style.Basis_Dialog_Style : style);
        if (Gravity.CENTER == gravity || Gravity.BOTTOM == gravity) {
            this.gravity = gravity;
        }
    }

    public BasisDialog setContentView(@LayoutRes int layout, int precentX, int precentY) {
        contentView = getLayoutInflater().inflate(layout, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(contentView, lp);
        Window window = getWindow();
        if (null != window) {
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.gravity = gravity;
            wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //根据 precentX precentY 处理弹框的高度
            if (precentX > 0) wl.width = ScreenUtil.getScreenPoint().x * precentX / 100;
            if (precentY > 0) wl.height = ScreenUtil.getScreenPoint().y * precentY / 100;
            window.setAttributes(wl);
            if (Gravity.BOTTOM == gravity) {
                window.setWindowAnimations(R.style.Basis_Style_Bottom_Menu_Anim);
            }
        }
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Logger.i(TAG, "onDismiss");
                if (null != listener) listener.onDismiss(dialog);
            }
        });
        return this;
    }

    public void observeDismiss(@Nullable OnDismissListener listener) {
        this.listener = listener;
    }

    public View getContentView() {
        return contentView;
    }

    public <V extends View> V getView(int id) {
        return contentView.findViewById(id);
    }

    /************ style dialog **********************/

    public static BasisDialog center(Activity activity, int layoutId) {
        return new BasisDialog(activity, Gravity.CENTER)
                .setContentView(layoutId, 74, -1);
    }

    public static BasisDialog bottom(Activity activity, int layoutId, int precentY) {
        return new BasisDialog(activity, Gravity.BOTTOM)
                .setContentView(layoutId, 100, precentY);
    }
}
