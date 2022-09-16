package com.qunli.demo;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.PreviewActivity;
import com.base.adapter.BaseAdapter;
import com.base.adapter.RcyHolder;
import com.base.adapter.RcySAdapter;
import com.base.kit.utils.ImageLoader;
import com.base.oklib.api.Method;
import com.base.ui.ListFragment;

public class RcyListFragment extends ListFragment<Meizi, Meizi> {
    private BaseAdapter<Meizi> mAdapter;
    private final int mCurPage = 1;


    @Override
    public int setLayoutId() {
        return R.layout.activity_rcyle_demo;
    }

    String url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/" + mCurPage + "/count/20";

    @Override
    public void initView() {
        request("", url, null, Method.get, true);
    }

    @Override
    protected RecyclerView.LayoutManager onSetLayoutManager() {
        return new GridLayoutManager(activity, 3);
    }

    @Override
    public BaseAdapter<Meizi> onSetAdapter() {
        if (null == mAdapter) {
            mAdapter = new RcySAdapter<Meizi>(activity, R.layout.item_mz) {
                @Override
                public void convert(RcyHolder holder, Meizi meizi, int position) {
                    ImageView imageView = holder.getView(R.id.img_content);
                    ImageLoader.loadUrl(imageView, meizi.getUrl(), R.mipmap.ic_launcher, ImageLoader.Size.SZ_250);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PreviewActivity.preview(activity, meizi.getUrl());
                        }
                    });
                }
            };
        }
        return mAdapter;
    }
}