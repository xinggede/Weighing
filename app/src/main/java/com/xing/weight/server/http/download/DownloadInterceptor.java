package com.xing.weight.server.http.download;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {

    private DownLoadListener progressListener;

    public DownloadInterceptor(DownLoadListener progressListener) {
        this.progressListener = progressListener;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new ProgressResponseBody(response.body(), progressListener)).build();
    }
}
