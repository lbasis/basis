package com.basis.ui.mvvm;

import com.bcq.net.api.Method;
import com.bcq.net.wrapper.interfaces.ILoadTag;
import com.bcq.net.wrapper.interfaces.IParse;

import java.util.Map;

public class ReqPam {
        protected ILoadTag tag;
        protected String url;
        protected Map<String, Object> params;
        protected IParse parser;
        protected Method method;
        protected boolean isRefresh;

        public ReqPam(ILoadTag dialog,
                      String url,
                      Map<String, Object> params,
                      IParse parser,
                      Method method,
                      boolean isRefresh) {
            this.tag = dialog;
            this.url = url;
            this.params = params;
            this.parser = parser;
            this.method = method;
            this.isRefresh = isRefresh;
        }
    }