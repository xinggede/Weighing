/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.weight.server.http.config;


import android.text.TextUtils;

import com.xing.weight.base.Constants;
import com.xing.weight.base.Prefs;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public final class HttpRequestInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
    public static String token = null;

    public static void resetToken() {
        token = null;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if (token == null) {
            token = Prefs.getInstance().getString(Constants.TOKEN);
//            if (TextUtils.isEmpty(token)) {
//                token = Prefs.getInstance().getString(Constants.TEMP_TOKEN);
//            }
        }
        Request request = chain.request();
        if (token == null) {
            return chain.proceed(request);
        }

        Request.Builder requestBuilder = request.newBuilder();
        if(request.method().equalsIgnoreCase("GET")){
            HttpUrl.Builder newUrlBuilder = request.url().newBuilder()
                    .addQueryParameter("token", token);
            requestBuilder.url(newUrlBuilder.build());
            return chain.proceed(requestBuilder.build());
        }

        RequestBody requestBody = request.body();
        if(requestBody == null){
            return chain.proceed(request);
        }
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            Charset charset = contentType.charset(UTF8);
            if(contentType.subtype().equalsIgnoreCase(MEDIA_TYPE.subtype())){
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                if(charset == null){
                    charset = UTF8;
                }
                String json = buffer.readString(charset);
                json = json.substring(0, json.length() - 1) + ",\"token\":\"" + token +"\"}";
                if(TextUtils.isEmpty(json)){
                    return chain.proceed(request);
                }
                requestBuilder.method(request.method(), RequestBody.create(json,contentType));
                return chain.proceed(requestBuilder.build());
            }
        }

        if (request.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oldFormBody = (FormBody) request.body();
            for (int i = 0; i < oldFormBody.size(); i++) {
                newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
            }
            newFormBody.add("token", token);
            requestBuilder.method(request.method(), newFormBody.build());
        }
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}
