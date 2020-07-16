package com.miaosha.service;

import com.miaosha.mapper.OrderMapper;
import com.miaosha.pojo.MiaoshaOrder;
import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.pojo.OrderInfo;
import com.miaosha.redis.Miaoshakey;
import com.miaosha.redis.RedisService;
import com.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;
    public MiaoshaOrder queryOrderByGoodsIdAndUserId(Long userId, Long goodsId) {
        MiaoshaOrder miaoshaOrder = this.redisService.get(Miaoshakey.getOrderKey, ""+userId+goodsId, MiaoshaOrder.class);
        if(null==miaoshaOrder) {
            miaoshaOrder = this.orderMapper.queryOrderByGoodsIdAndUserId(userId,goodsId);

        }
        return miaoshaOrder;
    }
    @Transactional
    public OrderInfo creatOrder(MiaoshaUser user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getGoodsPrice());
        orderInfo.setGoodsChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(miaoshaOrder);

        //把秒杀订单数据放到redis里面
        this.redisService.set(Miaoshakey.getOrderKey,
                ""+miaoshaOrder.getUserId()+miaoshaOrder.getGoodsId(), miaoshaOrder);
        return orderInfo;

    }

    public OrderInfo queryOrderByOrderId(Long orderId) {
        OrderInfo orderInfo = this.orderMapper.queryOrderByOrderId(orderId);
        return orderInfo;
    }


    public void deleteOrders() {
        orderMapper.deleteOrders();
        orderMapper.deleteMiaoshaOrders();

    }

    public Boolean updateOrder(String orderId) {
        int i= this.orderMapper.updateOrder(orderId);
        return i>0;
    }
}
