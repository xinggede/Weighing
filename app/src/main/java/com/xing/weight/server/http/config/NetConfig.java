package com.xing.weight.server.http.config;

import okhttp3.Interceptor;

public interface NetConfig {

    /**
     * 通用请求地址
     *
     * @return
     */
    String configBaseUrl();

    /**
     * 拦截器
     *
     * @return
     */
    Interceptor[] configInterceptors();

    /**
     * 连接超时时间
     *
     * @return
     */
    long configConnectTimeoutMills();

    /**
     * 读取超时时间
     *
     * @return
     */
    long configReadTimeoutMills();

    boolean retry();

    /**
     * 是否调试模式
     *
     * @return
     */
    boolean configLogEnable();


}
