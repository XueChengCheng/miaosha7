package com.miaosha.service;

import com.miaosha.mapper.GoodsMapper;
import com.miaosha.pojo.MiaoshaGoods;
import com.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    public List<GoodsVo> queryGoods() {
        return this.goodsMapper.queryGoods();
    }

    public GoodsVo queryGoodsByGoodSId(Long goodsId) {
        return this.goodsMapper.queryGoodsByGoodSId(goodsId);
    }

    public int reduceStock(Long goodsId) {
        return this.goodsMapper.reduceStock(goodsId);
    }

    public List<MiaoshaGoods> queryAllMiaoshaGoods() {
        return this.goodsMapper.queryAllMiaoshaGoods();
    }

    public void reduceStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsMapper.resetStock(g);
        }
    }
}
