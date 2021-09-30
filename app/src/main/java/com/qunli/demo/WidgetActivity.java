package com.qunli.demo;

import android.app.Dialog;
import android.view.View;

import com.basis.ui.BaseActivity;
import com.basis.widget.BasisDialog;
import com.basis.widget.EditorDialog;
import com.google.android.material.snackbar.Snackbar;
import com.kit.UIKit;
import com.kit.utils.KToast;
import com.kit.utils.Logger;
import com.kit.wapper.IResultBack;

public class WidgetActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int setLayoutId() {
        return R.layout.activity_widget;
    }

    @Override
    public void init() {
        getWrapBar().setTitle(R.string.str_widget).work();
        getView(R.id.search).setOnClickListener(this);
        getView(R.id.snack).setOnClickListener(this);
        getView(R.id.icons).setOnClickListener(this);
        getView(R.id.icons).setOnClickListener(this);
        getView(R.id.botton_dialog).setOnClickListener(this);
        getView(R.id.editor_dialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.search == id) {
            UIKit.startActivity(activity, SearchActivity.class);
        } else if (R.id.snack == id) {
            Snackbar.make(view, "已删除一个会话", Snackbar.LENGTH_LONG)
                    .setAction("撤销", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
//                    .setAction("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    })
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            switch (event) {
                                case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                    KToast.show("删除成功");
                                    break;
                                case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                    KToast.show("撤销了删除操作");
                                    break;
                            }
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {
                            super.onShown(snackbar);
                            Logger.i(TAG, "onShown");
                        }
                    }).show();
        } else if (R.id.icons == id) {
            UIKit.startActivity(activity, IconActivity.class);
        } else if (R.id.botton_dialog == id) {
            BasisDialog.bottom(activity, R.layout.basis_dialog_editor, 30).show();
//                    .setContentView(R.layout.basis_dialog_editor, 30)
//                    .observeDismiss(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialogInterface) {
//                            Logger.i(TAG, "onDismiss");
//                        }
//                    }).show();
        } else if (R.id.editor_dialog == id) {
            new EditorDialog(this, new
                    IResultBack<String>() {
                        @Override
                        public void onResult(String s) {
                            Logger.i(TAG, "onResult:" + s);
                        }
                    }).show();
        }
    }
}
