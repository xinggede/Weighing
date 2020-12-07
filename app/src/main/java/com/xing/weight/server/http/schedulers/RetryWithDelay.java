package com.xing.weight.server.http.schedulers;

import com.xing.weight.server.http.exception.ApiException;
import com.xing.weight.server.http.exception.CustomException;
import com.xing.weight.util.Tools;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .flatMap(throwable -> {
                    if (throwable instanceof ApiException) {
                        ApiException apiException = (ApiException) throwable;
                        if (apiException.getCode() == CustomException.PARSE_ERROR || apiException.getCode() == CustomException.HTTP_ERROR || apiException.getCode() == CustomException.TOKEN_ERROR) {
                            Tools.loge("解析或者返回码错误， 不进行重试");
                            return Observable.error(throwable);
                        }
                    }

                    if (++retryCount <= maxRetries) {
                        Tools.logd("retry millisecond: " + retryDelayMillis + ", retry count " + retryCount);
                        return Observable.timer(retryDelayMillis,
                                TimeUnit.MILLISECONDS);
                    }
                    return Observable.error(throwable);
                });
    }
}
