package com.miaosha.mapper;

import com.miaosha.pojo.MiaoshaOrder;
import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.pojo.OrderInfo;
import com.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper
public interface OrderMapper {
    @Select("select * from miaosha_order where user_id=#{mobile} and goods_id=#{goodsId}")
    MiaoshaOrder queryOrderByGoodsIdAndUserId(@Param(value="mobile") Long mobile, @Param(value="goodsId") Long goodsId);

    OrderInfo creatOrder(MiaoshaUser user, GoodsVo goodsVo);
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, goods_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{goodsChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id=#{orderId} ")
    OrderInfo queryOrderByOrderId(long orderId);

    @Delete("delete from order_info")
    void deleteOrders();
    @Delete("delete from miaosha_order")
    void deleteMiaoshaOrders();

    @Update("update order_info set status=1 ,pay_date=now() where id=#{orderId}")
    int updateOrder(String orderId);
}
