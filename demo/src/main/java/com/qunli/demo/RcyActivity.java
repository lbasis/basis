package com.qunli.demo;

import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.adapter.RcyAdapter;
import com.base.adapter.RcyHolder;
import com.base.adapter.RcySAdapter;
import com.base.adapter.interfaces.DataObserver;
import com.base.kit.cache.GsonUtil;
import com.base.kit.utils.ImageLoader;
import com.base.kit.utils.Logger;
import com.base.net.IRHolder;
import com.base.net.Request;
import com.base.oklib.api.Method;
import com.base.net.net.ListCallback;
import com.base.net.wrapper.interfaces.IPage;
import com.base.net.wrapper.interfaces.IResult;
import com.base.refresh.IRefresh;
import com.base.refresh.XRecyclerView;
import com.base.ui.BaseActivity;
import com.base.net.LoadTag;
import com.base.ui.RHolder;

import java.util.List;

public class RcyActivity extends BaseActivity {
    private XRecyclerView refresh;
    private RcyAdapter<Meizi> mAdapter;
    private int mCurPage = 1;
    private IRHolder holder;

    @Override
    public int setLayoutId() {
        return R.layout.activity_xrecycle;
    }

    public void init() {
        getWrapBar().setTitle(R.string.str_list_mv).work();
        refresh = findViewById(R.id.rv);
        holder = new RHolder((IRefresh) refresh);
        final GridLayoutManager layabouts = new GridLayoutManager(this, 2);
        refresh.setLayoutManager(layabouts);
        refresh.enableRefresh(true);
        refresh.enableLoad(true);
        refresh.setLoadListener(new IRefresh.LoadListener() {
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
        mAdapter = new RcySAdapter<Meizi>(this, R.layout.item_mz) {
            @Override
            public void convert(RcyHolder holder, Meizi meizi, int position) {
                ImageView imageView = holder.getView(R.id.img_content);
                ImageLoader.loadUrl(imageView, meizi.getUrl(), R.mipmap.ic_launcher, ImageLoader.Size.SZ_250);
            }
        };
        mAdapter.setDataObserver(new DataObserver() {
            @Override
            public void onObserve(int count) {
                Logger.e("SampleAdapter", "data len = " + count);
                holder.showType(count == 0 ? IRHolder.Type.none : IRHolder.Type.show);
            }
        });
        mAdapter.setRefreshView((RecyclerView) refresh);
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
                mAdapter.setData(meizis, isRefresh);
                IPage page = result.getExtra();
                Logger.e("AdapterActivity", "fetchGankMZ page = " + GsonUtil.obj2Json(page));
                if (null != page && page.getPage() >= page.getTotal()) {
                    mCurPage--;
                    refresh.enableLoad(false);
                } else {
                    refresh.enableLoad(true);
                }
            }

            @Override
            public void onError(int code, String errMsg) {
                Logger.e("AdapterActivity", "code = " + code + " message = " + errMsg);
                mAdapter.setData(null, false);
            }

            @Override
            public void onAfter() {
                if (null != refresh) {
                    refresh.loadComplete();
                    refresh.refreshComplete();
                }
            }
        });
    }
}