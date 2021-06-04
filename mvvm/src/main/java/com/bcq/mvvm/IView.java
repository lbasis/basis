package com.bcq.mvvm;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 通用holder接口
 */
public interface IView<T extends IView> extends IRelease {

    @Override
    void release();

    /**
     * 获取view
     *
     * @param viewId
     * @param <V>
     * @return view
     */
    <V extends View> V getView(int viewId);

    /**
     * 获取itemView
     */
    View rootView();

    /************************以下为辅助方法*********************/

    T setAlpha(int viewId, float value);

    T setVisible(int viewId, boolean visible);

    T setTag(int viewId, Object tag);

    T setChecked(int viewId, boolean checked);

    T setSelected(int viewId, boolean selected);

    T setText(int viewId, CharSequence text);

    T setTextColor(int viewId, int color);

    T setImageDrawable(int viewId, Drawable drawable);

    T setImageBitmap(int viewId, Bitmap bitmap);

    T setBackgroundColor(int viewId, int color);

    T setBackgroundResource(int viewId, int resource);

    T setOnClickListener(int viewId, View.OnClickListener ol);

    T setOnClickListener(int[] viewIds, View.OnClickListener ol);
}
