package com.miaosha.redis;

public class AccessKey extends BasePrefix {

	public AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	//有效期为5秒
	public static AccessKey getAccesskey5 = new AccessKey(5, "accesskey5");
	public static AccessKey getAccesskey10 = new AccessKey(10, "accesskey10");
	public static AccessKey getAccesskey20 = new AccessKey(20, "accesskey20");
	public static AccessKey getAccesskey30 = new AccessKey(30, "accesskey30");

	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}
}
