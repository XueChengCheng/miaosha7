package com.miaosha.result;

public class CodeMsg {
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	//登录模块 5002XX
	public static CodeMsg INPUT_MOBILE_ISNULL = new CodeMsg(500201, "输入手机号为空");
	public static CodeMsg MOBILE_ISERROR = new CodeMsg(500202, "输入手机号格式错误");
	public static CodeMsg PASSWORD_ISNULL = new CodeMsg(500202, "输入密码为空");
	public static CodeMsg MOBILE_ISNULL = new CodeMsg(500203, "该用户手机号不存在");
	public static CodeMsg INPUT_PASSWORDERROR = new CodeMsg(500204, "用户输入的手机或密码不正确");
	public static CodeMsg SESSION_TIMEOUT = new CodeMsg(500205, "登录信息过期");
	
	//商品模块 5003XX
	public static CodeMsg GOODSSTOCK_ISZERO = new CodeMsg(500301, "库存没了");
	
	//订单模块 5004XX
	public static CodeMsg ORDER_NOT_FIND = new CodeMsg(500401, "订单不存在");
	
	//秒杀模块 5005XX
	public static CodeMsg GOODS_ISREPEAT = new CodeMsg(500501, "商品已经秒杀过，不要秒杀了");

	public static CodeMsg MIAOSHA_ISERROR = new CodeMsg(500502, "非法操作，用户即将被锁定");

	public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500503, "秒杀错误");

	public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500504, "验证码错误");

	public static CodeMsg LIMIT_ACCESS_REACHED = new CodeMsg(500505, "访问太频繁，稍后再试");

	
	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
