package com.xing.weight.server.http;

import com.xing.weight.server.http.config.DefaultConfig;
import com.xing.weight.server.http.config.HttpRequestInterceptor;
import com.xing.weight.server.http.config.NetConfig;
import com.xing.weight.server.http.config.SSLSocketClient;
import com.xing.weight.server.http.converter.CusConverterFactory;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitManager {

    public static final String AUTHORIZATION = "authorization";
    public static final String LOGIN = "login";

    /**
     * 网络配置项
     */
    private static NetConfig mConfig = new DefaultConfig();

    private static Retrofit mRetrofit;

    private static OkHttpClient mOkHttpClient;

    /**
     * 重置网络配置
     *
     * @param config
     */
    public static void resetConfig(NetConfig config) {
        mConfig = config;
        mRetrofit = null;
        mOkHttpClient = null;
        getRetrofit();
    }

    /**
     * 获取Retrofit
     *
     * @return 获取Retrofit
     */
    private static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(mConfig.configBaseUrl())//配置BaseUrl
                    .client(getOkHttpClient())// 设置client
                    .addConverterFactory(CusConverterFactory.create());//gson转换器
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            mRetrofit = builder.build();
        }
        return mRetrofit;
    }


    /**
     * l
     * 获取OkHttpClient实例
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                //创建缓存文件，并设置缓存大小
//                Cache cache = new Cache(new File(Utils.getApp().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                if (mOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                    builder.cache(cache);
                    // 连接超时时间
                    builder.connectTimeout(mConfig.configConnectTimeoutMills(), TimeUnit.MILLISECONDS);
                    // 读取超时时间
                    builder.readTimeout(mConfig.configReadTimeoutMills(), TimeUnit.MILLISECONDS);
                    builder.retryOnConnectionFailure(mConfig.retry()).proxy(Proxy.NO_PROXY);
//                    builder.cookieJar(new CookiesManager());
                    builder.sslSocketFactory(SSLSocketClient.createSSLSocketFactory(), SSLSocketClient.createTrustAllManager())
                            .hostnameVerifier(new SSLSocketClient.TrustAllHostnameVerifier())
                            .followRedirects(true).followSslRedirects(true);

                    // 拦截器
                    Interceptor[] interceptors = mConfig.configInterceptors();
                    if (interceptors != null && interceptors.length > 0) {
                        for (Interceptor interceptor : interceptors) {
                            builder.addInterceptor(interceptor);
                        }
                    }

                    mOkHttpClient = RetrofitUrlManager
                            .getInstance()
                            .with(builder)
                            .build();
                    RetrofitUrlManager.getInstance().setDebug(mConfig.configLogEnable());
                    RetrofitUrlManager.getInstance().setGlobalDomain(mConfig.configBaseUrl());
//                    RetrofitUrlManager.getInstance().putDomain("authorization", URLConstants.GET_LOGIN_BASE_URL);
//                    RetrofitUrlManager.getInstance().putDomain("login", "http://120.238.131.27:8084/");
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 获取Service
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

    public static void resetToken() {
        HttpRequestInterceptor.resetToken();
    }
}
