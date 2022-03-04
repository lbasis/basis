package com.basis.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.basis.R;
import com.basis.dialog.BasisDialog;
import com.kit.UIKit;
import com.kit.wapper.IResultBack;


/**
 * 底部编辑弹框
 */
public class EditorDialog {
    IResultBack<String> resultBack;
    private InputMethodManager inputMethodManager;
    private EditText editText;
    private View complete;
    private BasisDialog dialog;

    public EditorDialog(Activity activity, IResultBack<String> resultBack) {
        this.resultBack = resultBack;
        dialog = BasisDialog.bottom(activity, R.layout.basis_dialog_editor, -1);
        dialog.setCanceledOnTouchOutside(false);
        inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        initView(dialog.getContentView());
    }

    public void show() {
        if (null != dialog) dialog.show();
    }

    public void dismiss() {
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        if (null != dialog) dialog.dismiss();
    }

    private void initView(View v) {
        editText = UIKit.getView(v, R.id.et_editor);
        complete = UIKit.getView(v, R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString().trim();
                dismiss();
                if (null != resultBack) resultBack.onResult(text);
            }
        });
        editText.requestFocus();
        editText.setFocusable(true);
        editText.post(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(editText, -1);
            }
        });
    }
}