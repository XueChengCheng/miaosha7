package com.miaosha.redis;

public class Miaoshakey extends BasePrefix {

	public Miaoshakey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
		  
	}
	
	public static Miaoshakey getGoodsCountKey = new Miaoshakey(0, "goodsCountKey");
	public static Miaoshakey getOrderKey = new Miaoshakey(0, "orderKey");
	public static Miaoshakey getIsGoodsOverKey = new Miaoshakey(0, "goodsOverKey");
	public static Miaoshakey getMiaoshaPathKey = new Miaoshakey(50, "miaoshaPathKey");
	public static Miaoshakey getMiaoshaVerifyCode = new Miaoshakey(1000*60, "miaoshaVerifyCode");
}
