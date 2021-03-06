package com.basis.net;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basis.BasisHelper;
import com.bcq.adapter.interfaces.DataObserver;
import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.interfaces.IHolder;
import com.bcq.net.net.NetRefresher;
import com.bcq.net.net.Page;
import com.bcq.net.wrapper.interfaces.IPage;
import com.bcq.net.wrapper.interfaces.IResult;
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
 * @className: UIController
 * @Description: 供列表显示页面使用的控制器
 */
public class Controller<ND, AD, VH extends IHolder> extends NetRefresher<ND> implements DataObserver {
    private final String TAG = "Controller";
    private IOperator<ND, AD, VH> operator;
    //适配器使用功能集合 泛型不能使用 T 接口返回类型有可能和适配器使用的不一致
    private List<AD> adapterList = new ArrayList<>();
    private IAdapte<AD, VH> mAdapter;
    private IRHolder holder;

    protected RecyclerView.LayoutManager onSetLayoutManager() {
        return new LinearLayoutManager(UIKit.getContext());
    }

    public Controller(IRHolder holder, Class<ND> tclazz, IOperator<ND, AD, VH> operator) {
        this(holder, tclazz, operator, BasisHelper.getPage());
    }

    public Controller(IRHolder holder, Class<ND> tclazz, IOperator<ND, AD, VH> operator, Page page) {
        super(tclazz, page, operator);
        this.holder = holder;
        this.operator = operator;
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
                requestAgain(true, operator);
            }

            @Override
            public void onLoad() {
                requestAgain(false, operator);
            }
        });
        holder.getNone().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAgain(true, operator);
            }
        });
    }

    /**
     * 重载：处理enable load
     *
     * @param result 结果集
     */
    @Override
    public void onResult(IResult.ObjResult<List<ND>> result) {
        super.onResult(result);
        IPage page = result.getExtra();
        boolean loadfull = null == page ? false : (page.getPage() >= page.getTotal());
        holder.getRefresh().enableLoad(loadfull);
    }

    @Override
    public void onAfter() {
        super.onAfter();
        if (null != holder && null != holder.getRefresh()) {
            holder.getRefresh().refreshComplete();
            holder.getRefresh().loadComplete();
        }
    }

    /**
     * 设置适配器数据回调
     *
     * @param netData   接口放回数据
     * @param isRefresh 是否刷新
     */
    @Override
    public void onRefreshData(List<ND> netData, boolean isRefresh) {
        /* 当页数据转换处理 */
        List<AD> preData = operator.onTransform(netData);
        if (isRefresh) adapterList.clear();
        if (null != preData) adapterList.addAll(preData);
        /* 设置适配器前 */
        List<AD> temp = operator.onPreSetData(adapterList);
        if (null != temp && !temp.isEmpty()) {
            if (null != holder) holder.showType(IRHolder.Type.show);
            // 此处已有缓存，不能再使用adapter中的缓存
            // 否则：load more 前的数据会叠加到adapter的数据集中
            mAdapter.setData(temp, true);
        } else {
            if (null != holder) holder.showType(IRHolder.Type.none);
        }
    }

    /**
     * 因适配器 removeItem 导致数据从有到无是回调
     * BsiAdapter.OnNoDataListeren 接口
     */
    @Override
    public void onObserve(int length) {
        Logger.e(TAG, "onObserve: len = " + length);
        if (null != holder) holder.showType(length == 0 ? IRHolder.Type.none : IRHolder.Type.show);
    }
}