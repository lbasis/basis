package com.bcq.mvvm;

import android.util.Log;
import android.view.View;


public abstract class ViewModel<M extends IModel> implements IViewModel<M> {
    protected final static String TAG = "ViewModel";
    private IView iView;
    private M model;

    @Deprecated
    public ViewModel() {
    }

    public ViewModel(View view) {
        this.iView = new ViewHolder(view);
    }

    @Override
    public void setIView(IView iView) {
        this.iView = iView;
    }

    @Override
    public IView getIView() {
        return iView;
    }

    @Override
    public <V extends View> V getView(int id) {
        return iView == null ? null : (V) iView.getView(id);
    }

    @Override
    public void setModel(M model) {
        this.model = model;
    }

    @Override
    @Deprecated
    public M getModel() {
        return model;
    }

    @Override
    public boolean hasModel() {
        return null != model;
    }

    @Override
    public void release() {
        if (null != iView) {
            iView.release();
            iView = null;
        }
        if (null != model) {
            model.release();
            model = null;
        }
    }

    @Override
    public void bind() {
        onConvert(ACTION_BIND, null);
    }

    @Override
    public final void refreshByCmd(int action, Object extra) {
        onConvert(action, extra);
    }

    public final void onConvert(int action, Object extra) {
        if (action < ACTION_LOAD) {
            Log.e(TAG, "the action can not ' < " + ACTION_LOAD + "' !");
            return;
        }
        if (null == iView) {
            Log.e(TAG, "the IView can not null !");
            return;
        }
        this.onConvert(model, action, extra);
    }

    public abstract void onConvert(M model, int action, Object extra);

}
