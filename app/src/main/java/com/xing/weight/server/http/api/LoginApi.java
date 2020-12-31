package com.xing.weight.server.http.api;

import com.xing.weight.bean.LoginResultInfo;
import com.xing.weight.bean.request.RequestLogin;
import com.xing.weight.server.http.response.BasicResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {

//    使用@body标签时不能用@FormUrlEncoded标签
//    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyStr);
//    Observable<String> saveLog(@Body DeviceBindBean bean);


    @POST("/api/login")
    Observable<BasicResponse<LoginResultInfo>> login(@Body RequestLogin requestBody);

    @POST("/api/register")
    Observable<BasicResponse<LoginResultInfo>> register(@Body RequestLogin requestBody);

    @POST("/api/findPwd")
    Observable<BasicResponse<LoginResultInfo>> findPwd(@Body RequestLogin requestBody);

    @GET("/api/getPhoneCode")
    Observable<BasicResponse<Object>> getSmsCode(@Query("phone") String phone);

    @POST("/api/logout")
    Observable<BasicResponse<Object>> logout();

}
