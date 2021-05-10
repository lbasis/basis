package com.basis;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.basis.ui.BaseActivity;
import com.basis.ui.IBasis;
import com.kit.utils.Logger;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @Author: BaiCQ
 * @ClassName: UI实例栈 包含activity 和 fragment
 * @CreateDate: 2019/3/29 16:43
 * @Description: UIStack
 */
public class UIStack {
    private final static String TAG = "UIStack";
    private final static UIStack instance = new UIStack();
    private static ArrayList<String> actions = new ArrayList<String>(4);
    //全局广播
    private BroadcastReceiver bdReceiver;
    private CustomerReceiver customerReceiver;
    private LinkedList<IBasis> iBases = new LinkedList<>();

    static {
        actions.add(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    private UIStack() {
    }

    public static UIStack getInstance() {
        return instance;
    }

    /**
     * 添加自定义全局广播
     *
     * @param cusActions 意图actions集合
     * @param receiver   回调
     */
    public void setCustomerReceiver(String[] cusActions, CustomerReceiver receiver) {
        if (null == cusActions || null == receiver) return;
        customerReceiver = receiver;
        boolean update = false;
        for (String action : cusActions) {
            if (!actions.contains(action)) {
                actions.add(action);
                update = true;
            }
        }
        if (!update) return;
        //actions 变更后，重新注册全局广播
        if (null != bdReceiver) {
            BroadcastUtil.unregisterReceiver(bdReceiver);
        }
        bdReceiver = new UIReceiver();
        BroadcastUtil.registerReceiver(bdReceiver, actions);
    }

    public synchronized void add(IBasis iBasis) {
        iBases.add(iBasis);
        if (null == bdReceiver) {//说明释放啦
            bdReceiver = new UIReceiver();
            BroadcastUtil.registerReceiver(bdReceiver, actions);
        }
        Logger.e(TAG, "add : size = " + iBases.size() + " ibase:" + iBasis.getClass().getSimpleName());
    }

    public synchronized void remove(IBasis remove) {
        iBases.remove(remove);
        if (iBases.isEmpty()) {
            if (null != bdReceiver) {
                BroadcastUtil.unregisterReceiver(bdReceiver);
                bdReceiver = null;
            }
        }
        Logger.e(TAG, "remove : size = " + iBases.size() + " ibase:" + remove.getClass().getSimpleName());
    }

    public Activity getTopActivity() {
        int len = iBases.size();
        for (int i = len - 1; i >= 0; i--) {
            IBasis base = iBases.get(i);
            if (base instanceof Activity) {
                return (BaseActivity) base;
            }
        }
        return null;
    }

    public boolean isTaskTop(Activity activity) {
        if (null == activity) return false;
        int len = iBases.size();
        Activity top = null;
        for (int i = len - 1; i >= 0; i--) {
            IBasis base = iBases.get(i);
            if (base instanceof Activity) {
                top = (Activity) base;
                break;
            }
        }
        boolean isTaskTop = null != top && top == activity;
        Logger.e(TAG, "isTaskTop:" + isTaskTop);
        return isTaskTop;
    }

    public synchronized void exit() {
        if (null != bdReceiver) {
            BroadcastUtil.unregisterReceiver(bdReceiver);
            bdReceiver = null;
        }
        int len = iBases.size();
        for (int i = len - 1; i >= 0; i--) {
            IBasis base = iBases.get(i);
            if (base instanceof Activity) {
                Activity act = (Activity) base;
                if (!act.isFinishing()) {
                    act.finish();
                }
            }
        }
    }

    public static class UIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                instance.setResultForAll();
            } else {
                instance.onCustomerReceive(context, intent);
            }
        }
    }

    private void setResultForAll() {
        for (IBasis a : iBases) {
            if (a instanceof Activity) {
                a.onNetChange();
            }
        }
    }

    private void onCustomerReceive(Context context, Intent intent) {
        if (null != customerReceiver) customerReceiver.onReceive(context, intent);
    }
}