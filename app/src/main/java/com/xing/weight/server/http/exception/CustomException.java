package com.xing.weight.server.http.exception;

import android.net.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.xing.weight.server.http.response.BasicResponse;
import com.xing.weight.util.Tools;
import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;


/**
 * Created by Zaifeng on 2018/2/28.
 * 本地异常处理。包括解析异常等其他异常
 */

public class CustomException {

    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;

    public static final int TOKEN_ERROR = 1004;

    public static ApiException handleException(Throwable e) {
        Tools.loge("handleException: " + e.getMessage());
        ApiException ex;
        if (e instanceof ApiException) {
            return (ApiException) e;
        }
        if (e instanceof HttpException) {
            Response response = ((HttpException) e).response();
            if (response != null) {
                if (response.code() == 401) {
                    return new ApiException(TOKEN_ERROR, "token过期");
                }
                if (response.code() == 500) {
                    return new ApiException(HTTP_ERROR, "服务器维护中...");
                }
                ResponseBody body = response.errorBody();
                try {
                    assert body != null;
                    BasicResponse basicResponse = new Gson().fromJson(body.string(), BasicResponse.class);
                    ex = new ApiException(HTTP_ERROR, basicResponse.getMessage());
                } catch (IOException exc) {
                    exc.printStackTrace();
                    ex = new ApiException(HTTP_ERROR, e.getMessage());
                }
            } else {
                ex = new ApiException(HTTP_ERROR, e.getMessage());
            }
            return ex;
        } else if (e instanceof JsonSyntaxException || e instanceof MalformedJsonException || e instanceof JSONException || e instanceof ParseException) {
            //解析错误
            ex = new ApiException(PARSE_ERROR, "数据格式错误");
            return ex;
        } else if (e instanceof ConnectException) {
            //网络错误
            ex = new ApiException(NETWORK_ERROR, "网络异常");
            return ex;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            ex = new ApiException(NETWORK_ERROR, "服务器应答超时");
            return ex;
        } else if (e instanceof NullPointerException) {
            //连接错误
            ex = new ApiException(PARSE_ERROR, "数据格式错误");
            return ex;
        } else {
            //未知错误
            ex = new ApiException(UNKNOWN, e.getMessage());
            return ex;
        }
    }
}
