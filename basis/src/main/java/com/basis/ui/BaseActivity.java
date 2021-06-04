package com.basis.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.basis.UIStack;
import com.basis.widget.ActionWrapBar;
import com.basis.widget.interfaces.IWrapBar;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IViewModel;
import com.kit.utils.Logger;

/**
 * @author: BaiCQ
 * @ClassName: BaseActivity
 * @date: 2018/8/17
 * @Description: 基于IBasis实现的UI组件基类
 * 1.统一封装ActionBar
 * 2.针对常用的api的封装，如：getView()
 * 3.针对finish()相关的统一封装onBackCode()
 */
public abstract class BaseActivity<M extends IModel> extends AppCompatActivity implements IBasis<M> {
    protected final String TAG = this.getClass().getSimpleName();
    protected BaseActivity activity;
    private IWrapBar wrapBar;
    private IViewModel viewModel;

    @Override
    protected void onDestroy() {
        UIStack.getInstance().remove(activity);
        if (null != viewModel) {
            viewModel.release();
            viewModel = null;
        }
        super.onDestroy();
    }

    @Override
    @Deprecated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        UIStack.getInstance().add(activity);
        viewModel = setViewModel();
        setContentView(viewModel.getIView().rootView());
        //init wapp
        wrapBar = new ActionWrapBar(activity).work();
        init();
        // 设置model 绑定
        viewModel.bind();
    }

    public abstract IViewModel setViewModel();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return wrapBar.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            Logger.e(TAG, "back !");
            onBackCode();
            return true;
        } else {
            return wrapBar.onOptionsItemSelected(item);
        }
    }

    public IWrapBar getWrapBar() {
        return wrapBar;
    }

    @Override
    public abstract void init();

    public IViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onRefresh(Object obj) {
    }

    @Override
    public void onNetChange() {
    }

    @Override
    public void onBackPressed() {
        onBackCode();
    }

    /**
     * 统一处理activity 返回事件
     */
    public void onBackCode() {
        finish();
    }
}