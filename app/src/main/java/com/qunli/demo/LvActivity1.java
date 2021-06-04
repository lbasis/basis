package com.qunli.demo;

import android.view.View;
import android.widget.ImageView;

import com.basis.net.LoadTag;
import com.basis.ui.BaseActivity;
import com.bcq.adapter.SampleAdapter;
import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.listview.LvHolder;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IViewModel;
import com.bcq.mvvm.ViewModel;
import com.bcq.net.Request;
import com.bcq.net.api.Method;
import com.bcq.net.net.ListCallback;
import com.bcq.net.wrapper.interfaces.IResult;
import com.bcq.refresh.IRefresh;
import com.kit.UIKit;
import com.kit.utils.ImageLoader;
import com.kit.utils.Logger;

import java.util.List;

public class LvActivity1 extends BaseActivity {
    private int mCurPage = 1;
    private LvViewModel viewModel;
    private final LvModel model = new LvModel();


    @Override
    public IViewModel setViewModel() {
        viewModel = new LvViewModel();
        model.adapter = new SampleAdapter<Meizi, LvHolder>(this, R.layout.item_mz) {
            @Override
            public void convert(LvHolder holder, Meizi meizi, int position, int layoutId) {
                ImageView imageView = (ImageView) holder.getView(R.id.img_content);
                ImageLoader.loadUrl(imageView, meizi.getUrl(), R.mipmap.ic_launcher, ImageLoader.Size.SZ_250);
            }
        };
        viewModel.setModel(model);
        viewModel.setListener(new IRefresh.LoadListener() {
            @Override
            public void onRefresh() {
                mCurPage = 1;
                fetchGankMZ(true, false);
            }

            @Override
            public void onLoad() {
                mCurPage++;
                fetchGankMZ(false, false);
            }
        });
        return viewModel;
    }

    public void init() {
        getWrapBar().setTitle(R.string.str_list_mv).work();
        fetchGankMZ(true, true);
    }

    private void fetchGankMZ(final boolean isRefresh, boolean show) {
        String url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/" + mCurPage + "/count/20";
        Request.request(show ? new LoadTag(this) : null, url, null, Method.get, new ListCallback<Meizi>(Meizi.class) {
            @Override
            public void onResult(IResult.ObjResult<List<Meizi>> result) {
                super.onResult(result);
                List<Meizi> meizis = result.getResult();
                int len = null == result.getResult() ? 0 : meizis.size();
                Logger.e("AdapterActivity", "fetchGankMZ len = " + len);
//                mAdapter.setData(meizis, isRefresh);
                model.data = meizis;
                viewModel.bind();
                if (result.getExtra()) {
                    mCurPage--;
                }
            }

            @Override
            public void onError(int code, String errMsg) {
                super.onError(code, errMsg);
            }

            @Override
            public void onAfter() {
                viewModel.onComplete();
            }
        });
    }

    private static class LvModel implements IModel {
        public IAdapte<Meizi, LvHolder> adapter;
        public List<Meizi> data;

        @Override
        public void release() {
            data.clear();
        }
    }

    private static class LvViewModel extends ViewModel<LvModel> {
        private IRefresh.LoadListener listener;

        LvViewModel() {
            super(UIKit.inflate(R.layout.activity_lv));
        }

        public void setListener(IRefresh.LoadListener listener) {
            this.listener = listener;
        }

        public void onComplete() {
            IRefresh refresh = (IRefresh) getIView().getView(R.id.rv);
            if (null != refresh) {
                refresh.loadComplete();
                refresh.refreshComplete();
            }
        }

        @Override
        public void onConvert(LvModel model, int action, Object extra) {
            IRefresh refresh = (IRefresh) getIView().getView(R.id.rv);
            if (ACTION_BIND == action) {
                refresh.enableRefresh(true);
                refresh.enableLoad(true);
                refresh.setLoadListener(new IRefresh.LoadListener() {
                    @Override
                    public void onRefresh() {
                        listener.onRefresh();
                    }

                    @Override
                    public void onLoad() {
                        listener.onLoad();
                    }
                });
                if (null != model.adapter) {
                    model.adapter.setRefreshView((View) refresh);
                    model.adapter.setData(model.data, true);
                }
            } else if (0 == action) {
                model.adapter.setData(model.data, false);
            } else if (1 == action) {
                model.adapter.setData(model.data, true);
            }
        }
    }

}