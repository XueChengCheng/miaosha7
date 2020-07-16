package com.miaosha.service;

import com.miaosha.pojo.MiaoshaOrder;
import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.pojo.OrderInfo;
import com.miaosha.redis.Miaoshakey;
import com.miaosha.redis.RedisService;
import com.miaosha.util.UUIDUtil;
import com.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
public class MiaoshaService {
    @Autowired
    private  GoodsService goodsService;

    @Autowired
    private  OrderService orderService;
   @Autowired
   private RedisService redisService;
    @Transactional
    public OrderInfo doMiaosha(MiaoshaUser user, GoodsVo goodsVo) {
        OrderInfo orderInfo=null;
     //1.减少库存(miaosha_goods)
        //解决超卖问题  使修改方法有个返回值
        int i=this.goodsService.reduceStock(goodsVo.getGoodsId());
        //若返回值不大于0  则不能生成订单
     if(i>0){
         orderInfo=this.orderService.creatOrder(user,goodsVo);

     }else {
         //  -1 0
         setGoodsOver(goodsVo.getId());
     }
        //2.生成订单详情，生成秒杀订单
         return orderInfo;
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = this.orderService.queryOrderByGoodsIdAndUserId(userId, goodsId);
        //三种情况
        if(order!=null) {
            return order.getOrderId();  //秒杀成功
        }else {
            Boolean isOver = getGoodsOver(goodsId);

            if(isOver) {  //秒杀失败  true  ,说明订单已经存在
                return -1;
            }else {  //
                return  0; //继续轮询
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        this.redisService.set(Miaoshakey.getIsGoodsOverKey,""+goodsId, true);  //true 是失败

    }
    private Boolean getGoodsOver(Long goodsId) {
        return this.redisService.exists(Miaoshakey.getIsGoodsOverKey,""+goodsId);

    }
    public void reset(List<GoodsVo> goodsList) {
        goodsService.reduceStock(goodsList);
        orderService.deleteOrders();
    }


    public String creatPath(MiaoshaUser user, Long goodId) {
         String path= UUIDUtil.uuid();
        this.redisService.set(Miaoshakey.getMiaoshaPathKey, "" + user.getId() +"_"+ goodId, path);
        return path;
    }


    //生成验证码--------------------------------------------------

    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 90;
        int height = 32;

        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(Miaoshakey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if(user == null || goodsId <=0) {
            return false;
        }
        Integer codeOld = redisService.get(Miaoshakey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(Miaoshakey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        int num4 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        char op3 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3+ op3 + num4;
        return exp;
    }

}
