package com.qunli.demo;

import android.net.Uri;
import android.view.View;

import com.base.BroadcastUtil;
import com.base.PreviewActivity;
import com.base.kit.CommUtil;
import com.base.kit.UIKit;
import com.base.kit.utils.Logger;
import com.base.ui.BaseActivity;
import com.base.widget.ActionWrapBar;
import com.base.widget.dialog.BasisDialog;
import com.base.widget.dialog.DialogBuilder;
import com.base.widget.dialog.WXDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        getView(R.id.indicator).setOnClickListener(this);

        getView(R.id.recycle).setOnClickListener(this);
        getView(R.id.listview).setOnClickListener(this);

        getView(R.id.listui).setOnClickListener(this);
        getView(R.id.recy_ui).setOnClickListener(this);
        getView(R.id.preview).setOnClickListener(this);
        getView(R.id.net_test).setOnClickListener(this);

        getView(R.id.recy_frag).setOnClickListener(this);
        getView(R.id.list_frag).setOnClickListener(this);

        getView(R.id.widget).setOnClickListener(this);
        getView(R.id.fab_bar).setOnClickListener(this);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 600; i++) {
            buffer.append("#123456789");
        }
        Logger.e(buffer.toString());
        initBarWrapper();
    }

    private boolean hide = false;

    public void initBarWrapper() {
        getWrapBar().setTitle(R.string.str_home)
                .setBackHide(true)
                .addOptionMenu("弹框")
                .setOnMenuSelectedListener(new ActionWrapBar.OnMenuSelectedListener() {
                    @Override
                    public void onItemSelected(int position) {
                        if (0 == position) {
                            new DialogBuilder(activity)
                                    .setEnableTitle(true)
                                    .setMessage("默认风格！")
                                    .defaultsStyle(null)
                                    .buildFD()
                                    .show();
                            new DialogBuilder(activity)
                                    .setEnableTitle(true)
                                    .setMessage("取消风格！")
                                    .cancelStyle()
                                    .buildFD()
                                    .show();
                            new DialogBuilder(activity)
                                    .setEnableTitle(true)
                                    .setMessage("确定风格！")
                                    .sureStyle(null)
                                    .buildFD()
                                    .show();
                            new DialogBuilder(activity)
                                    .setEnableTitle(true)
                                    .setMessage("删除风格！")
                                    .deleteStyle(null)
                                    .buildFD()
                                    .show();
                            BasisDialog.bottom(activity, R.layout.layout_bottom_menu, 40).show();
                            BasisDialog.center(activity, R.layout.layout_bottom_menu).show();
                        } else if (1 == position) {

                        }
                    }
                }).work();
    }

    WXDialog dialog;

    @Override
    public void onClick(View v) {
        if (CommUtil.isQuickClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.indicator:
                BroadcastUtil.sendBroadcast(BApplication.CUS_ACTION, "什么情况");
//                UIKit.startActivity(activity, IndicatorActivity.class);
                break;
            case R.id.recycle:
                UIKit.startActivity(activity, RcyActivity.class);
                break;
            case R.id.recy_ui:
                UIKit.startActivity(activity, RcyListActivity.class);
                break;
            case R.id.preview:
                Uri uri = Uri.parse("https://images.unsplash.com/photo-1577643816920-65b43ba99fba?ixlib=rb-1.2.1&auto=format&fit=crop&w=3300&q=80");
                PreviewActivity.preview(this, uri);
                break;
            case R.id.net_test:
                UIKit.startActivity(activity, NetTestActvivty.class);
                break;
            case R.id.list_frag:
                UIKit.startActivityByBasis(activity, SamepleFragmentActivity.class, false);
                break;
            case R.id.recy_frag:
                UIKit.startActivityByBasis(activity, SamepleFragmentActivity.class, true);
                break;
            case R.id.widget:
                UIKit.startActivity(activity, WidgetActivity.class);
                break;
            case R.id.fab_bar:
                hide = !hide;
                getWrapBar().setHide(hide).work();
                break;
        }
    }

}
