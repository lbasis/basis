package com.basis.ui.mvvm;

import com.basis.ui.IListRefresh;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IViewModel;
import com.bcq.net.wrapper.interfaces.ILoadTag;

/**
 * 刷新ViewModel统通用接口
 *
 * @param <ND>
 */
public interface IRefVM<ND, M extends IModel> extends IViewModel<M>, IListRefresh<ILoadTag,ND> {
}