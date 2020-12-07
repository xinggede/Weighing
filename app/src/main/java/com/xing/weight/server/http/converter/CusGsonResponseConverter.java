package com.xing.weight.server.http.converter;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.MalformedJsonException;
import com.xing.weight.util.Tools;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 自定义的类型解析器
 * <p>用来对付不安常理出牌的后台数据格式</p>
 *
 * @param <T>
 */
public class CusGsonResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    public CusGsonResponseConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String body = value.string();
            Tools.logd("result: " + body);
            T t = adapter.fromJson(body);
            return t;
        } catch (JsonSyntaxException | MalformedJsonException e) {
            //GSON解析失败，换成XML解析
            e.printStackTrace();
            throw e;
        } finally {
            value.close();
        }
    }

}