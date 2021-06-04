package com.basis.ui;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basis.net.BaseOperator;
import com.basis.net.IOperator;
import com.basis.net.LoadTag;
import com.basis.ui.mvvm.RefPam;
import com.basis.ui.mvvm.RefViewModel;
import com.basis.ui.mvvm.ReqPam;
import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.interfaces.IHolder;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IViewModel;
import com.bcq.net.api.Method;
import com.bcq.net.wrapper.interfaces.IParse;
import com.kit.UIKit;
import com.kit.utils.ObjUtil;

import java.util.List;
import java.util.Map;

/**
 * @param <ND> 接口数据类型
 * @param <AD> 适配器数据类型 一般情况：和ND类型一致
 * @param <VH> 适配器的holder类型 IRefresh的类型是Listview VH是LvHolder，若是RecylerView VH是RcyHolder
 */
public abstract class ListActivity<ND, AD, VH extends IHolder> extends BaseActivity implements IListRefresh<String, ND, AD, VH> {
    private Class<ND> tClass;

    protected RecyclerView.LayoutManager onSetLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    public void onConvert(IModel model, int action, Object extra) {
    }

    @Override
    public final IViewModel setViewModel() {
        tClass = (Class<ND>) ObjUtil.getTType(getClass())[0];
        View view = UIKit.inflate(onSetLayoutId());
        return new RefViewModel(view, tClass, onSetOperater()) {
            @Override
            protected RecyclerView.LayoutManager onSetLayoutManager() {
                return ListActivity.this.onSetLayoutManager();
            }

            @Override
            public void onConvert(IModel model, int action, Object extra) {
                super.onConvert(model, action, extra);
                ListActivity.this.onConvert(model,action,extra);
            }
        };
    }

    /**
     * 设置默认处理器
     */
    @Override
    public IOperator<ND, AD, VH> onSetOperater() {
        return new BaseOperator() {
            @Override
            public IAdapte onSetAdapter() {
                return ListActivity.this.onSetAdapter();
            }
        };
    }

    /*************** IListRefresh 实现 ***************/
    public abstract int onSetLayoutId();

    @Override
    public abstract void init();

    public abstract IAdapte<AD, VH> onSetAdapter();


    @Override
    public void refresh(List<ND> netData, boolean isRefresh) {
        getViewModel().refreshByCmd(IViewModel.ACTION_REFRESH, new RefPam(isRefresh, netData));
    }

    @Override
    public void request(String tag, String api, Map<String, Object> params, Method method, boolean isRefresh) {
        getViewModel().refreshByCmd(
                IViewModel.ACTION_REQUEST,
                new ReqPam(new LoadTag(activity, tag), api, params, null, method, true));
    }

    @Override
    public void request(String tag, String api, Map<String, Object> params, IParse parser, Method method, boolean isRefresh) {
        getViewModel().refreshByCmd(
                IViewModel.ACTION_REFRESH,
                new ReqPam(new LoadTag(activity, tag), api, params, parser, method, true));
    }
}