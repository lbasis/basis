package com.qunli.demo;

import android.net.Uri;
import android.view.View;

import com.basis.PreviewActivity;
import com.basis.ui.BaseActivity;
import com.basis.widget.ActionWrapBar;
import com.basis.widget.BottomDialog;
import com.basis.widget.WXDialog;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IViewModel;
import com.bcq.mvvm.ViewModel;
import com.kit.UIKit;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public IViewModel setViewModel() {
        return new ViewModel(UIKit.inflate(R.layout.activity_main)) {
            @Override
            public void onConvert(IModel model, int action, Object extra) {
                iView.setOnClickListener(new int[]{
                        R.id.indicator,
                        R.id.recycle,
                        R.id.listview,
                        R.id.listui,
                        R.id.recy_ui,
                        R.id.preview,
                        R.id.net_test,
                        R.id.recy_frag,
                        R.id.list_frag,
                        R.id.widget,
                        R.id.fab_bar
                }, MainActivity.this);
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview:
                Uri uri = Uri.parse("https://images.unsplash.com/photo-1577643816920-65b43ba99fba?ixlib=rb-1.2.1&auto=format&fit=crop&w=3300&q=80");
                PreviewActivity.preview(activity, uri);
                break;
            case R.id.listview:
                UIKit.startActivity(activity, LvActivity.class);
                break;
            case R.id.listui:
                UIKit.startActivity(activity, LvListActivity.class);
                break;
        }
    }

    @Override
    public void init() {
        getWrapBar().setTitle(R.string.str_home)
                .setBackHide(true)
                .addOptionMenu("弹框")
                .setOnMenuSelectedListener(new ActionWrapBar.OnMenuSelectedListener() {
                    @Override
                    public void onItemSelected(int position) {
                        if (0 == position) {
                            new WXDialog(activity)
                                    .setMessage("默认风格！")
                                    .defalutStyle(true, null).show();
                            new WXDialog(activity)
                                    .setMessage("取消风格！")
                                    .cancelStyle(true).show();
                            new WXDialog(activity)
                                    .setMessage("确定风格！")
                                    .sureStyle(true, null).show();
                            new WXDialog(activity)
                                    .setMessage("删除风格！")
                                    .deleteStyle(true, null).show();
                            new BottomDialog(activity)
                                    .setContentView(R.layout.layout_bottom_menu, 40)
                                    .show();
                        } else if (1 == position) {

                        }
                    }
                }).work();
    }

}
