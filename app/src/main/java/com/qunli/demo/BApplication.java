package com.qunli.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.basis.BasisHelper;
import com.basis.CustomerReceiver;
import com.bcq.net.wrapper.OkHelper;
import com.bcq.net.wrapper.Wrapper;
import com.bcq.net.wrapper.interfaces.IParse;
import com.bcq.refresh.RefreshHelper;
import com.bcq.refresh.progress.Style;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kit.UIKit;
import com.kit.cache.GsonUtil;
import com.kit.utils.Logger;


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
            parser = new BqIParse();
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

    public static class BqIParse implements IParse<Wrapper> {

        @Override
        public Wrapper parse(int httpcode, String json) {
            Wrapper info = new Wrapper();
            info.setCode(httpcode);
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
