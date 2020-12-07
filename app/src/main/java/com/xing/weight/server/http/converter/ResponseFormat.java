package com.xing.weight.server.http.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseFormat {

    String JSON = "json";
    String XML = "xml";
    String STR = "string";

    String value() default JSON;
}
