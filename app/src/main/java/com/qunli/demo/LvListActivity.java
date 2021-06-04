package com.qunli.demo;

import android.view.View;
import android.widget.ImageView;

import com.basis.PreviewActivity;
import com.basis.ui.ListActivity;
import com.bcq.adapter.SampleAdapter;
import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.listview.LvHolder;
import com.bcq.net.api.Method;
import com.kit.utils.ImageLoader;

public class LvListActivity extends ListActivity<Meizi, Meizi, LvHolder> {
    private final int mCurPage = 1;
    String url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/" + mCurPage + "/count/20";

    @Override
    public int onSetLayoutId() {
        return R.layout.activity_list_demo;
    }

    @Override
    public void init() {
        getWrapBar().setTitle(R.string.str_list_mv).work();
        request(null, url, null, Method.get, true);
    }

    @Override
    public IAdapte<Meizi, LvHolder> onSetAdapter() {
        return new SampleAdapter<Meizi, LvHolder>(this, R.layout.item_mz) {
            @Override
            public void convert(LvHolder holder, Meizi meizi, int position, int layoutId) {
                ImageView imageView = (ImageView) holder.getView(R.id.img_content);
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
}