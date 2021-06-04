package com.basis.ui;

import com.basis.net.IOperator;
import com.bcq.adapter.interfaces.IHolder;
import com.bcq.net.api.Method;
import com.bcq.net.wrapper.interfaces.IParse;

import java.util.List;
import java.util.Map;

/**
 * 列表展示ui组件的统一主动处理接口
 *
 * @param <ND> 网络数据解析封装实体
 * @param <T>  Tag
 */
public interface IListRefresh<T, ND, AD, VH extends IHolder> {

    /**
     * 手动刷新适配器数据
     *
     * @param netData   接口放回数据
     * @param isRefresh 是否刷新
     */
    void refresh(List<ND> netData, boolean isRefresh);

    /**
     * 请求列表数据
     *
     * @param tag       进度条显示msg
     * @param api       mUrl
     * @param params    参数 注意：不包含page
     * @param method    Post/get
     * @param isRefresh 是否刷新
     */
    void request(T tag, String api, Map<String, Object> params, Method method, boolean isRefresh);

    /**
     * 请求列表数据
     *
     * @param tag       进度条显示msg
     * @param api       mUrl
     * @param params    参数 注意：不包含page
     * @param method    Post/get
     * @param parser    解析器 自定义解析器
     * @param isRefresh 是否刷新
     */
    void request(T tag, String api, Map<String, Object> params, IParse parser, Method method, boolean isRefresh);

    /**
     * 设置刷新处理器
     *
     * @return
     */
    IOperator<ND, AD, VH> onSetOperater();

    int onSetLayoutId();
}
