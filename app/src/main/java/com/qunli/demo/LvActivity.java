package com.qunli.demo;

import android.widget.ImageView;

import com.basis.net.BaseOperator;
import com.basis.ui.BaseActivity;
import com.basis.ui.mvvm.RefViewModel;
import com.basis.ui.mvvm.ReqPam;
import com.bcq.adapter.SampleAdapter;
import com.bcq.adapter.interfaces.IAdapte;
import com.bcq.adapter.listview.LvHolder;
import com.bcq.mvvm.IViewModel;
import com.bcq.net.api.Method;
import com.kit.UIKit;
import com.kit.utils.ImageLoader;

public class LvActivity extends BaseActivity {
    private int mCurPage = 1;
    private String url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/" + mCurPage + "/count/20";

    @Override
    public IViewModel setViewModel() {
        return new RefViewModel(UIKit.inflate(R.layout.activity_lv), Meizi.class,
                new BaseOperator<Meizi, Meizi, LvHolder>() {
                    @Override
                    public IAdapte<Meizi, LvHolder> onSetAdapter() {
                        return new SampleAdapter<Meizi, LvHolder>(activity, R.layout.item_mz) {
                            @Override
                            public void convert(LvHolder holder, Meizi meizi, int position, int layoutId) {
                                ImageView imageView = (ImageView) holder.getView(R.id.img_content);
                                ImageLoader.loadUrl(imageView, meizi.getUrl(), R.mipmap.ic_launcher, ImageLoader.Size.SZ_250);
                            }
                        };
                    }
                });
    }

    public void init() {
        getWrapBar().setTitle(R.string.str_list_mv).work();
        ReqPam req = new ReqPam(null, url, null, null, Method.get, true);
        getViewModel().refreshByCmd(IViewModel.ACTION_REQUEST, req);
    }

}