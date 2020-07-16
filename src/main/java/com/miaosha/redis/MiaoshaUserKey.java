package com.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix {

	public static int   tokenexpireSeconds=3600*24*2;
	public MiaoshaUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static MiaoshaUserKey get_tokenKey=new MiaoshaUserKey(tokenexpireSeconds,"tk");
	public static MiaoshaUserKey get_userByIdKey=new MiaoshaUserKey(0,"tk");
}
