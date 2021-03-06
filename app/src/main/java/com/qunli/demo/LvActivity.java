package com.qunli.demo;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basis.net.IRHolder;
import com.basis.ui.BaseActivity;
import com.basis.net.LoadTag;
import com.basis.ui.RHolder;
import com.bcq.adapter.SampleAdapter;
import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.listview.LvHolder;
import com.bcq.net.Request;
import com.bcq.net.api.Method;
import com.bcq.net.net.ListCallback;
import com.bcq.net.wrapper.interfaces.IPage;
import com.bcq.net.wrapper.interfaces.IResult;
import com.bcq.refresh.IRefresh;
import com.kit.utils.ImageLoader;
import com.kit.utils.Logger;

import java.util.List;

public class LvActivity extends BaseActivity {
    private IRefresh refresh;
    private IAdapte<Meizi, LvHolder> mAdapter;
    private int mCurPage = 1;
    private IRHolder holder;

    @Override
    public int setLayoutId() {
        return R.layout.activity_lv;
    }

    public void init() {
        getWrapBar().setTitle(R.string.str_list_mv).work();
        refresh = findViewById(R.id.rv);
        if (refresh instanceof RecyclerView) {
            final GridLayoutManager layoutmanager = new GridLayoutManager(this, 2);
            ((RecyclerView) refresh).setLayoutManager(layoutmanager);
        }
        holder = new RHolder(refresh);
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
        mAdapter = new SampleAdapter<Meizi, LvHolder>(this, R.layout.item_mz) {

            @Override
            public void convert(LvHolder holder, Meizi meizi, int position, int layoutId) {
                ImageView imageView = holder.getView(R.id.img_content);
                ImageLoader.loadUrl(imageView, meizi.getUrl(), R.mipmap.ic_launcher, ImageLoader.Size.SZ_250);
            }

            @Override
            public void onObserve(int length) {
                Logger.e("SampleAdapter", "data len = " + length);
                holder.showType(length == 0 ? IRHolder.Type.none : IRHolder.Type.show);
            }
        };
        mAdapter.setRefreshView((View) refresh);
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
                if (null != page && page.getPage() >= page.getTotal()) {
                    mCurPage--;
                    refresh.enableLoad(false);
                }else {
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