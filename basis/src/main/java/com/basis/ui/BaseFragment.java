package com.basis.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basis.UIStack;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IViewModel;
import com.kit.utils.Logger;

/**
 * @author: BaiCQ
 * @ClassName: BaseFragment
 * @date: 2018/8/17
 * @Description: Fragment 的基类
 */
public abstract class BaseFragment<M extends IModel> extends Fragment implements IBasis<M> {
    protected final String TAG = this.getClass().getSimpleName();
    protected BaseActivity activity;
    private boolean init = false;//init 和 onRefresh()的执行的先后问题
    private IViewModel<M> viewModel;

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) context;
        UIStack.getInstance().add(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UIStack.getInstance().remove(this);
        if (null != viewModel) {
            viewModel.release();
            viewModel = null;
        }
        init = false;
        Logger.e(TAG, "onDetach");
    }

    @Deprecated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = setViewModel();
        Logger.e(TAG, "onCreateView");
        return viewModel.getIView().rootView();
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.e(TAG, "onViewCreated");
        init();
        init = true;
    }

    @Override
    public abstract IViewModel<M> setViewModel();

    @Override
    public abstract void init();


    public IViewModel<M> getViewModel() {
        return viewModel;
    }

    /**
     * 首次刷新尽量先于init执行
     */
    @Override
    public void onRefresh(Object obj) {
        Logger.e(TAG, "onRefresh");
    }

    @Override
    public void onNetChange() {
        Logger.e(TAG, "onRefresh");
    }

    public boolean isInit() {
        return init;
    }
}
