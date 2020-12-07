package com.xing.weight.server.http.download;

import com.xing.weight.server.http.config.DefaultConfig;
import com.xing.weight.server.http.config.NetConfig;
import com.xing.weight.server.http.config.SSLSocketClient;
import com.xing.weight.server.http.converter.CusConverterFactory;
import com.xing.weight.server.http.schedulers.RetryWithDelay;
import com.xing.weight.server.http.schedulers.SchedulerProvider;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownloadManager {

    /**
     * 网络配置项
     */
    private static NetConfig mConfig = new DefaultConfig();

    private static Retrofit mRetrofit;

    private static OkHttpClient mOkHttpClient;

    private static DownLoadApi downLoadApi;

    private static HashMap<String, Disposable> hashMap = new HashMap<>();
    private static List<String> urls = new ArrayList<>();

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

            downLoadApi = mRetrofit.create(DownLoadApi.class);
        }
        return mRetrofit;
    }

    /**
     * 获取OkHttpClient实例
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (DownloadManager.class) {
                if (mOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    // 连接超时时间
                    builder.connectTimeout(mConfig.configConnectTimeoutMills(), TimeUnit.MILLISECONDS);
                    // 读取超时时间
                    builder.readTimeout(mConfig.configReadTimeoutMills(), TimeUnit.MILLISECONDS);
                    builder.retryOnConnectionFailure(true).proxy(Proxy.NO_PROXY);
                    builder.sslSocketFactory(SSLSocketClient.createSSLSocketFactory(), SSLSocketClient.createTrustAllManager())
                            .hostnameVerifier(new SSLSocketClient.TrustAllHostnameVerifier())
                            .followRedirects(true).followSslRedirects(true);

//                    DownloadInterceptor downloadInterceptor = new DownloadInterceptor(null);
//                    builder.addInterceptor(downloadInterceptor);
                    mOkHttpClient = builder.build();
                }
            }
        }
        return mOkHttpClient;
    }

    public static void downLoad(String url, String savePath, long range, DownLoadListener downLoadListener) {
        if (downLoadApi == null) {
            getRetrofit();
        }

        if (urls.contains(url)) {
            return;
        }
        urls.add(url);

        File saveFile = new File(savePath);
        String totalLength = "-";
        if (saveFile.exists()) {
            totalLength += saveFile.length();
        }
        String r = "bytes=" + range + totalLength;

        Disposable disposable = downLoadApi.download(r, url).compose(SchedulerProvider.getInstance().applySchedulers())
                .retryWhen(new RetryWithDelay(2, 3000))
                .flatMap((Function<ResponseBody, ObservableSource<Integer>>) responseBody -> Observable.create((ObservableOnSubscribe<Integer>) observableEmitter -> {
                    RandomAccessFile randomAccessFile = null;
                    InputStream inputStream = null;
                    long total = range;
                    long responseLength = 0;
                    try {
                        byte[] buf = new byte[2048];
                        int len = 0;
                        responseLength = responseBody.contentLength();
                        inputStream = responseBody.byteStream();
                        File file = new File(savePath);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        randomAccessFile = new RandomAccessFile(file, "rwd");
                        if (range == 0) {
                            randomAccessFile.setLength(responseLength);
                        }
                        randomAccessFile.seek(range);

                        int progress = 0;
                        int lastProgress = 0;

                        while ((len = inputStream.read(buf)) != -1) {
                            randomAccessFile.write(buf, 0, len);
                            total += len;
                            lastProgress = progress;
                            progress = (int) (total * 100 / randomAccessFile.length());
                            if (progress > 0 && progress != lastProgress) {
                                observableEmitter.onNext(progress);
                            }
                        }
                        observableEmitter.onComplete();
                    } catch (Exception e) {
                        observableEmitter.onError(e);
                    } finally {
                        try {
                            if (randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).compose(SchedulerProvider.getInstance().applySchedulers())).subscribe(progress -> {
                    if (downLoadListener != null) {
                        downLoadListener.onDownloadProgress(progress);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (downLoadListener != null) {
                        downLoadListener.onDownLoadError(throwable.getMessage());
                    }
                    urls.remove(url);
                }, () -> {
                    if (downLoadListener != null) {
                        downLoadListener.onDownLoadSuccess(savePath);
                    }
                    urls.remove(url);
                });
        hashMap.put(url, disposable);
    }

    public static void downLoadApk(String url, String savePath, DownLoadListener downLoadListener) {
        if (downLoadApi == null) {
            getRetrofit();
        }

        if (urls.contains(url)) {
            return;
        }
        urls.add(url);

        Disposable disposable = downLoadApi.download(url).compose(SchedulerProvider.getInstance().applySchedulers())
                .retryWhen(new RetryWithDelay(2, 3000))
                .flatMap((Function<ResponseBody, ObservableSource<Integer>>) responseBody -> Observable.create((ObservableOnSubscribe<Integer>) observableEmitter -> {
                    RandomAccessFile randomAccessFile = null;
                    InputStream inputStream = null;
                    long responseLength = 0;
                    long total = 0;
                    try {
                        byte[] buf = new byte[4096];
                        int len = 0;
                        responseLength = responseBody.contentLength();
                        inputStream = responseBody.byteStream();
                        File file = new File(savePath);
                        if (file.exists()) {
                            file.delete();
                        }
                        randomAccessFile = new RandomAccessFile(file, "rwd");
                        randomAccessFile.setLength(responseLength);

                        int progress = 0;
                        int lastProgress = 0;

                        while ((len = inputStream.read(buf)) != -1) {
                            randomAccessFile.write(buf, 0, len);
                            total += len;
                            lastProgress = progress;
                            progress = (int) (total * 100 / randomAccessFile.length());
                            if (progress > 0 && progress != lastProgress) {
                                observableEmitter.onNext(progress);
                            }
                        }
                        observableEmitter.onComplete();
                    } catch (Exception e) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onError(e);
                        }
                    } finally {
                        try {
                            if (randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).compose(SchedulerProvider.getInstance().applySchedulers())).subscribe(progress -> {
                    if (downLoadListener != null) {
                        downLoadListener.onDownloadProgress(progress);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (downLoadListener != null) {
                        downLoadListener.onDownLoadError(throwable.getMessage());
                    }
                    urls.remove(url);
                }, () -> {
                    if (downLoadListener != null) {
                        downLoadListener.onDownLoadSuccess(savePath);
                    }
                    urls.remove(url);
                });
        hashMap.put(url, disposable);
    }

    public static void clearAllDownload() {
        for (String s : hashMap.keySet()) {
            Disposable disposable = hashMap.get(s);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        hashMap.clear();
        urls.clear();
    }

    public static void cancelDownload(String url) {
        Disposable disposable = hashMap.get(url);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        hashMap.remove(url);
        urls.remove(url);
    }

}
