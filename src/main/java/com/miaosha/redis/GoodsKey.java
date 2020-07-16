package com.miaosha.redis;



public class GoodsKey extends BasePrefix {

	public GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
		 
	}
	
	public static  GoodsKey getGoodsListKey = new GoodsKey(60, "goodsListKey");
	public static  GoodsKey getGoodsDetailKey = new GoodsKey(60, "goodsDetailKey");

}
