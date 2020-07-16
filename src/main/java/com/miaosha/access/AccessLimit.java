package com.miaosha.access;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

	int seconds(); //有效期的秒数，比如5秒 
	int maxCount(); //访问的上限次数 
	boolean needLogin() default true;  //是否校验 ，默认是 
}
