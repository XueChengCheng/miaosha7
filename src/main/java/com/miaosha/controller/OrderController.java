package com.miaosha.controller;

import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.pojo.OrderInfo;
import com.miaosha.result.CodeMsg;
import com.miaosha.result.Result;
import com.miaosha.service.GoodsService;
import com.miaosha.service.OrderService;
import com.miaosha.vo.GoodsVo;
import com.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private GoodsService goodsServie;
	@Autowired
	private OrderService orderService;
	
 	@RequestMapping("/detail")
    @ResponseBody
   public  Result<OrderDetailVo> detail(@RequestParam("orderId") Long orderId, MiaoshaUser user) {
 		//1首先判断用户是否登录，如果未登录，则返回
 		if(user==null) {
 			return Result.error(CodeMsg.SESSION_TIMEOUT);
 		}
 		
 		//2查询 订单OrderInfo
 		OrderInfo order = this.orderService.queryOrderByOrderId(orderId);
 		if(order==null) {
 			return Result.error(CodeMsg.ORDER_NOT_FIND);
 		}
 		
 		//3查询 goodsVo  
 		GoodsVo goods = this.goodsServie.queryGoodsByGoodSId(order.getGoodsId());
 		
 		OrderDetailVo vo = new OrderDetailVo();
 		
 		vo.setGoods(goods);
 		vo.setOrder(order);
        return Result.success(vo);
    }
}
