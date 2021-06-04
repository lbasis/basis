package com.basis.net;

import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.interfaces.IHolder;
import com.bcq.net.net.IOpe;

import java.util.List;

/**
 * Model 处理抽象接口
 *
 * @param <ND> net data 接口数据类型
 * @param <AD> adapter data 适配器数据类型 一般情况：和ND类型一致
 * @param <VH> 适配器的view holder类型
 */
public abstract class BaseOperator<ND, AD, VH extends IHolder> implements IOperator<ND, AD, VH> {

    @Override
    public void onCustomerRequestAgain(boolean refresh) {
    }

    @Override
    public void onError(int status, String errMsg) {
    }

    /**
     * 数据转换
     * Fix：问题1：适配数据和网络数据类型不一致，如：网络文件列表 和本地文件列表共存在列表中
     *
     * @param netData 当次（当前页）网络数据
     */
    public List<AD> onTransform(List<ND> netData) {
        return (List<AD>) netData;
    }

    /**
     * 设置adapter数据前回调
     * 一般分页排序功能获取拼接数据时使用,或在在没有实时数据时切换到缓存数据等
     *
     * @param netData 设置给adapter的所有（包含所有页码）数据
     * @return 返回数据集合直接设置给adapter，会执行showViewType()修改ui
     */
    public List<AD> onPreSetData(List<AD> netData) {
        return netData;
    }

    public abstract IAdapte<AD, VH> onSetAdapter();
}