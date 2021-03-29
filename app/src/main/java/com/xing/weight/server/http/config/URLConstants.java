package com.xing.weight.server.http.config;


import com.xing.weight.BuildConfig;

public class URLConstants {
    private static final String BASE_HTTP_URL = "http://pbox.51cprint.com";
    private static final String TEST_BASE_HTTP_URL = /*"http://192.168.2.120";*/"http://pbox.51cprint.com";
    public static final String GET_HTTP_BASE_URL = BuildConfig.DEBUG ? TEST_BASE_HTTP_URL : BASE_HTTP_URL;

}
