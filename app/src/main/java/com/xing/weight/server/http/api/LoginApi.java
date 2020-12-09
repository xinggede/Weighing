package com.xing.weight.server.http.api;

import com.xing.weight.bean.LoginResultInfo;
import com.xing.weight.bean.request.RequestLogin;
import com.xing.weight.server.http.response.BasicResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {

//    使用@body标签时不能用@FormUrlEncoded标签
//    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyStr);
//    Observable<String> saveLog(@Body DeviceBindBean bean);


    @POST("/api/login")
    Observable<BasicResponse<LoginResultInfo>> login(@Body RequestLogin requestBody);



}
