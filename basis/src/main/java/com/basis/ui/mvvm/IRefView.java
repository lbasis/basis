package com.basis.ui.mvvm;

import android.view.View;

import com.bcq.mvvm.IView;
import com.bcq.refresh.IRefresh;

public interface IRefView {

    enum Type {show, none}

    IRefresh getRefresh();

    View getNone();

    View getShow();

    void showType(Type type);

    void onComplete();
}