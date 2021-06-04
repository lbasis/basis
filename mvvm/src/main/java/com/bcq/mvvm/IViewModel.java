package com.bcq.mvvm;

import android.view.View;

/**
 * 通用ViewModel接口
 */
public interface IViewModel<M extends IModel> extends IRelease {
    int ACTION_BIND = -1;
    int ACTION_REQUEST = -2;
    int ACTION_REFRESH = -3;
    int ACTION_LOAD = -4;


    @Override
    void release();

    void setIView(IView view);

    IView getView();

    @Deprecated
    <V extends View> V getView(int id);

    void setModel(M model);

    @Deprecated
    M getModel();

    boolean hasModel();

    void bind();

    void refreshByCmd(int action, Object extra);
}
