package com.xing.weight.server.http.response;

import com.xing.weight.server.http.exception.ApiException;
import com.xing.weight.server.http.exception.CustomException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * 对返回的数据进行处理，区分异常的情况。
 */

public class ResponseTransformer {

    public static <T> ObservableTransformer<BasicResponse<T>, T> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>());
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends BasicResponse<T>>> {

        @Override
        public ObservableSource<? extends BasicResponse<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<BasicResponse<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(BasicResponse<T> basicResponse) throws Exception {
            int code = basicResponse.getCode();
            String message = basicResponse.getMessage();
            if (code == 200) {
                if(basicResponse.getData() == null){
                    return (ObservableSource<T>) Observable.just("");
                }
                return Observable.just(basicResponse.getData());
            } else {
                return Observable.error(new ApiException(CustomException.HTTP_ERROR, message));
            }
        }
    }

}
