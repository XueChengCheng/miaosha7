package com.miaosha.mapper;

import com.miaosha.pojo.MiaoshaGoods;
import com.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {
    @Select("select g.*,mg.goods_id,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from goods g left join miaosha_goods mg on g.id=mg.goods_id")
    List<GoodsVo> queryGoods();

    @Select("select g.*,mg.goods_id,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from goods g left join miaosha_goods mg on g.id=mg.goods_id where g.id=#{goodsId}")
    GoodsVo queryGoodsByGoodSId(Long goodsId);

    @Update("update miaosha_goods SET stock_count=stock_count-1 WHERE goods_id=#{goodsId} and stock_count>0")
    int reduceStock(Long goodsId);
    @Select("select * from miaosha_goods")
    List<MiaoshaGoods> queryAllMiaoshaGoods();

    @Update("update miaosha_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
    void resetStock(MiaoshaGoods g);
}
