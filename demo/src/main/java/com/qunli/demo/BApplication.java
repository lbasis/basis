package com.qunli.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.base.BasisHelper;
import com.base.CustomerReceiver;

import com.base.kit.UIKit;
import com.base.kit.cache.GsonUtil;
import com.base.kit.utils.Logger;
import com.base.net.wrapper.OkHelper;
import com.base.oklib.Wrapper;
import com.base.net.wrapper.interfaces.IParse;
import com.base.refresh.RefreshHelper;
import com.base.refresh.progress.Style;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class BApplication extends Application {
    private static IParse parser;
    public static String CUS_ACTION = "com.ql_demo.test.customer_action";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        Logger.setDebug(true);
        OkHelper.setDebug(true);
        if (null == parser) {
            parser = new BqParser();
        }
        OkHelper.get().setDefaultParser(parser);
        RefreshHelper.setDefaultStyle(Style.LineScale);
        BasisHelper.setCustomerReceiver(new String[]{CUS_ACTION}, new CustomerReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String param = (String) intent.getSerializableExtra(UIKit.KEY_BASE);
                Logger.e("CustomerReceiver", "action = " + action + " param = " + param);
            }
        });
    }

    public static IParse<Wrapper> getParser() {
        return parser;
    }

    public static class BqParser implements IParse<Wrapper> {

        @Override
        public Wrapper parse(int httpCode, String json) {
            Wrapper info =  Wrapper.fromHttpCode(httpCode);
            Logger.e("BqParser", "json = " + json);
            JsonElement result = JsonParser.parseString(json);
            if (result instanceof JsonObject) {
                JsonObject resulObj = (JsonObject) result;
                info.setBody(resulObj.get("data"));
                info.setPage(resulObj.get("page").getAsInt(), resulObj.get("page_count").getAsInt());
            }
            Logger.e("BqParser", "wrapper = " + GsonUtil.obj2Json(info));
            return info;
        }

        @Override
        public boolean ok(int code) {
            return code == 200 || code == 1;
        }
    }
}
