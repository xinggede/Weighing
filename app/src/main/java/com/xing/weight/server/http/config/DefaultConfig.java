package com.xing.weight.server.http.config;


import com.xing.weight.BuildConfig;
import com.xing.weight.util.Tools;

import okhttp3.Interceptor;

public class DefaultConfig implements NetConfig {

    @Override
    public String configBaseUrl() {
        return URLConstants.GET_HTTP_BASE_URL;
    }

    @Override
    public Interceptor[] configInterceptors() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Tools.logd(message);
            }
        });
        // 四个级别：NONE,BASIC,HEADER,BODY
        // BASEIC:请求/响应行
        // HEADER:请求/响应行 + 头
        // BODY:请求/响应航 + 头 + 体
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (configLogEnable()) {
            return new Interceptor[]{new HttpRequestInterceptor(), interceptor};
        }
        return new Interceptor[]{new HttpRequestInterceptor()};
    }

    @Override
    public long configConnectTimeoutMills() {
        return 10000;
    }

    @Override
    public long configReadTimeoutMills() {
        return 183000;
    }

    @Override
    public boolean retry() {
        return false;
    }

    @Override
    public boolean configLogEnable() {
        return BuildConfig.DEBUG;
    }
}
