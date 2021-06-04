package com.basis.ui.mvvm;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basis.BasisHelper;
import com.basis.net.IOperator;
import com.bcq.adapter.interfaces.DataObserver;
import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.interfaces.IHolder;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IViewModel;
import com.bcq.mvvm.ViewModel;
import com.bcq.net.net.NetRefresher;
import com.bcq.net.net.Page;
import com.bcq.refresh.IRefresh;
import com.kit.UIKit;
import com.kit.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <ND> 适配器数据类型
 * @param <AD> 接口数据类型
 * @param <VH> 接口数据类型
 * @author: BaiCQ
 * @createTime: 2017/1/13 11:38
 * @className: RefViewModel
 * @Description: 供列表显示页面使用的控制器
 */
public class RefViewModel<ND, AD, VH extends IHolder> extends ViewModel<IModel> implements DataObserver /*, IRefVM<ND, IModel>*/ {
    private final String TAG = "RefreshViewModel";
    private IOperator<ND, AD, VH> operator;
    //适配器使用功能集合 泛型不能使用 T 接口返回类型有可能和适配器使用的不一致
    private List<AD> adapterList = new ArrayList<>();
    private IAdapte<AD, VH> mAdapter;
    private IRefView holder;
    private NetRefresher<ND> refresher;

    protected RecyclerView.LayoutManager onSetLayoutManager() {
        return new LinearLayoutManager(UIKit.getContext());
    }

    public RefViewModel(View view, Class<ND> tclazz, IOperator<ND, AD, VH> operator) {
        this(view, tclazz, operator, BasisHelper.getPage());
    }

    public RefViewModel(View view, Class<ND> tclazz, IOperator<ND, AD, VH> operator, Page page) {
        this.operator = operator;
        this.holder = new RefView(view);
        setIView((RefView) holder);
        this.refresher = new NetRefresher<ND>(tclazz, page, operator) {
            @Override
            protected void onRefreshData(List<ND> data, boolean refresh) {
                refreshByCmd(IViewModel.ACTION_REFRESH, new RefPam(refresh, data));
            }

            @Override
            public void onAfter() {
                if (null != holder) holder.onComplete();
            }
        };
        initialize();
    }

    private void initialize() {
        holder.getRefresh().enableRefresh(true);
        holder.getRefresh().enableLoad(true);
        if (holder.getRefresh() instanceof RecyclerView) {
            ((RecyclerView) holder.getRefresh()).setLayoutManager(onSetLayoutManager());
        }
        mAdapter = operator.onSetAdapter();
        mAdapter.setRefreshView((View) holder.getRefresh());
        //注意此处一定要在setRefreshView后，否则别覆盖
        mAdapter.setDataObserver(this);
        holder.getRefresh().setLoadListener(new IRefresh.LoadListener() {
            @Override
            public void onRefresh() {
                // TODO: 2021/6/4
//                if (null != refresher) refresher.requestAgain(true);
            }

            @Override
            public void onLoad() {
//                if (null != refresher) refresher.requestAgain(false);
            }
        });
        holder.getNone().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != refresher) refresher.requestAgain(true);
            }
        });
    }

    /**
     * 因适配器 removeItem 导致数据从有到无是回调
     * BsiAdapter.OnNoDataListeren 接口
     */
    @Override
    public void onObserve(int length) {
        Logger.e(TAG, "onObserve: len = " + length);
        if (null != holder) holder.showType(length == 0 ? IRefView.Type.none : IRefView.Type.show);
    }

    @Override
    public void onConvert(IModel model, int action, Object extra) {
        // TODO: 2021/6/4 统一处理处理refreshByCmd
        //  1、ACTION_REFRESH 刷新适配器 extra类型：RefPam
        //  2、ACTION_REQUEST request请求 extra类型：ReqPam
        if (ACTION_REFRESH == action && extra instanceof RefPam) {
            RefPam param = (RefPam) extra;
            refresh(param.data, param.refresh);
        } else if (ACTION_REQUEST == action && extra instanceof ReqPam) {
            ReqPam param = (ReqPam) extra;
            if (null != refresher)
                refresher.request(param.tag, param.url, param.params, param.parser, param.method, param.isRefresh);
        }
    }

    /**
     * 设置适配器数据回调
     *
     * @param netData   接口放回数据
     * @param isRefresh 是否刷新
     */
    private void refresh(List<ND> netData, boolean isRefresh) {
        /* 当页数据转换处理 */
        List<AD> preData = operator.onTransform(netData);
        if (isRefresh) adapterList.clear();
        if (null != preData) adapterList.addAll(preData);
        /* 设置适配器前 */
        List<AD> temp = operator.onPreSetData(adapterList);
        if (null != temp && !temp.isEmpty()) {
            if (null != holder) holder.showType(IRefView.Type.show);
            mAdapter.setData(temp, isRefresh);
        } else {
            if (null != holder) holder.showType(IRefView.Type.none);
        }
    }
}