package com.xing.weight.server.http.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownLoadApi {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("Range") String range, @Url String url);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
