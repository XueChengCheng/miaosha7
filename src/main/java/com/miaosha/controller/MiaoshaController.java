package com.miaosha.controller;

import com.miaosha.access.AccessLimit;
import com.miaosha.pojo.MiaoshaGoods;
import com.miaosha.pojo.MiaoshaOrder;
import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.pojo.OrderInfo;
import com.miaosha.rabbit.MQSender;
import com.miaosha.rabbit.MiaoshaMessage;
import com.miaosha.redis.AccessKey;
import com.miaosha.redis.Miaoshakey;
import com.miaosha.redis.RedisService;
import com.miaosha.result.CodeMsg;
import com.miaosha.result.Result;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaService;
import com.miaosha.service.OrderService;
import com.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
  @Autowired
  private RedisService redisService;

    Map<Long,Boolean> localOverMap = new HashMap<Long,Boolean>();
    @Autowired
    private MQSender sender;
    //项目启动的时候加载
    @Override
    public void afterPropertiesSet() throws Exception {
        List<MiaoshaGoods> goodsList = this.goodsService.queryAllMiaoshaGoods();
        for(MiaoshaGoods goods:goodsList) {
            this.redisService.set(Miaoshakey.getGoodsCountKey, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);//false 默认是有库存的
        }
    }
    //原始秒杀方法
    /*@RequestMapping("/do_miaosha")
    public String domiaosha(MiaoshaUser user, Long goodsId, ModelMap modelMap){
       if(null==user){
            return "login";
        }
        System.out.println("压力测试的id"+goodsId);
        //1.检查是否有库存
        GoodsVo goodsVo=this.goodsService.queryGoodsByGoodSId(goodsId);
        if(goodsVo.getStockCount()<=0){
            modelMap.put("errmsg","已经没有库存了");
            return "miaosha_fail";
        }
        //2.检查当前用户是否已参加过秒杀
        MiaoshaOrder miaoshaOrder=this.orderService.queryOrderByGoodsIdAndUserId(user.getId(),goodsId);
        if(null!=miaoshaOrder){
            modelMap.put("errmsg", "已经参加过秒杀了，不要在购买了");
            return "miaosha_fail";
        }
        //3.减少库存   生成订单详情
        OrderInfo orderInfo=this.miaoshaService.doMiaosha(user,goodsVo);
        modelMap.put("goods",goodsVo);
        modelMap.put("orderInfo",orderInfo);
        return "order_detail";
    }*/

    //秒杀静态页面
   /* @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> domiaosha(MiaoshaUser user, Long goodsId, ModelMap modelMap){
        System.out.println("秒杀页面的用户"+user);
       if(null==user){
            return Result.error(CodeMsg.SESSION_TIMEOUT);
        }

        //1.检查是否有库存
        GoodsVo goodsVo=this.goodsService.queryGoodsByGoodSId(goodsId);
        if(goodsVo.getStockCount()<=0){

            return Result.error(CodeMsg.GOODSSTOCK_ISZERO);
        }
        //2.检查当前用户是否已参加过秒杀
        MiaoshaOrder miaoshaOrder=this.orderService.queryOrderByGoodsIdAndUserId(user.getId(),goodsId);
        System.out.println("已经秒杀是否"+miaoshaOrder);
        if(null!=miaoshaOrder){

            return Result.error(CodeMsg.GOODS_ISREPEAT);
        }
        //3.减少库存   生成订单详情
        OrderInfo orderInfo=this.miaoshaService.doMiaosha(user,goodsVo);

        return Result.success(orderInfo);
    }*/

    //用消息队列来完成秒杀  创建订单
 @RequestMapping(value = "/{path}/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> domiaosha(MiaoshaUser user, Long goodsId,
                                     @PathVariable(value = "path")String path){
        System.out.println("秒杀页面的用户"+user);
       if(null==user){
            return Result.error(CodeMsg.SESSION_TIMEOUT);
        }

     String cathPath = this.redisService.
             get(Miaoshakey.getMiaoshaPathKey, ""+user.getId()+"_"+goodsId, String.class);
     if(!path.equals(cathPath)) {
         return Result.error(CodeMsg.MIAOSHA_ISERROR);
     }
     //10   设置redis预减  ，降低redis的访问量localOverMap
     if(localOverMap.get(goodsId)) {  //true说明就没有库存了
         return Result.error(CodeMsg.GOODSSTOCK_ISZERO);
     }

     //3  缓存里预减（提前减少一下）库存，库存是否小于0，返回错误信息，如果不小于继续后面操作
     //i--
     Long cacheStock = this.redisService.decr(Miaoshakey.getGoodsCountKey, ""+goodsId);
     if(cacheStock<=0) {  //<=-1
         localOverMap.put(goodsId, true);
         return Result.error(CodeMsg.GOODSSTOCK_ISZERO);
     }
     // 4  判断该商品是否已经秒杀过，这个同样查询缓存（service调用就可以）
     MiaoshaOrder miaoshaOrder = this.orderService.queryOrderByGoodsIdAndUserId(user.getId(),goodsId);
     if(null!=miaoshaOrder) {
         return Result.error(CodeMsg.GOODS_ISREPEAT);
     }
     //5   准备一个实体类，封装秒杀用户信息和秒杀商品id
     MiaoshaMessage mm = new MiaoshaMessage();
     mm.setGoodsId(goodsId);
     mm.setUser(user);
     //6   准备使用消息队列  入队，使用driect 队列方式
     this.sender.miaoshaSend(mm);
     return Result.success(0);
       /* //1.检查是否有库存
        GoodsVo goodsVo=this.goodsService.queryGoodsByGoodSId(goodsId);
        if(goodsVo.getStockCount()<=0){

            return Result.error(CodeMsg.GOODSSTOCK_ISZERO);
        }
        //2.检查当前用户是否已参加过秒杀
        MiaoshaOrder miaoshaOrder=this.orderService.queryOrderByGoodsIdAndUserId(user.getId(),goodsId);
        System.out.println("已经秒杀是否"+miaoshaOrder);
        if(null!=miaoshaOrder){

            return Result.error(CodeMsg.GOODS_ISREPEAT);
        }
        //3.减少库存   生成订单详情
        OrderInfo orderInfo=this.miaoshaService.doMiaosha(user,goodsVo);*/
    }


    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */

    @RequestMapping(value="/result",method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(MiaoshaUser miaoshaUser ,@RequestParam(value="goodsId") long goodsId ) {
        //如果没有登录跳转到登录页面
        if(miaoshaUser==null) {
            return Result.error(CodeMsg.SESSION_TIMEOUT);
        }
        long result = this.miaoshaService.getMiaoshaResult(miaoshaUser.getId(),goodsId);

        return Result.success(result);
    }


    //重置数据
    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.queryGoods();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(Miaoshakey.getGoodsCountKey, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(Miaoshakey.getOrderKey);
        redisService.delete(Miaoshakey.getIsGoodsOverKey);
        miaoshaService.reset(goodsList);
        return Result.success(true);
    }

      //获取秒杀地址
      @AccessLimit(seconds=5, maxCount=5, needLogin=true)
      @RequestMapping(value = "/path",method = RequestMethod.GET)
      @ResponseBody
    public Result<String> getPath(MiaoshaUser user, Long goodId,
                                  @RequestParam(value="verifyCode", defaultValue="0")int verifyCode,
                                  HttpServletRequest request){
        if(null==user){
            return Result.error(CodeMsg.SESSION_TIMEOUT);
        }
        //比如实现5秒内访问5次限制
        // 如果上面返回一个boolean类型，如果是true ，我们认为已经达到上限5秒5次, m秒n次
     /* boolean flag=shangxian(request,user,goodId);
        if(flag){
            return Result.error(CodeMsg.LIMIT_ACCESS_REACHED);
        }*/
      //**************************************************
        boolean check = miaoshaService.checkVerifyCode(user, goodId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path=this.miaoshaService.creatPath(user,goodId);
        return Result.success(path);
    }

    private boolean shangxian(HttpServletRequest request, MiaoshaUser user, Long goodId) {
        //路径、登录的用户，和秒杀的商品，针对这个作为限流的条件  ,总体作为  Key  ,保存到redis里面，保持时间为5秒
        String uri=request.getRequestURI();
        String key=uri+"_"+user.getId()+"_"+goodId;
        //访问的次数
        Integer count = this.redisService.get(AccessKey.getAccesskey5, key, Integer.class);
        if(count==null) {
            this.redisService.set(AccessKey.getAccesskey5, key, 1);  //第一次访问保存访问次数为1
        }else if(count<5) {
            this.redisService.incr(AccessKey.getAccesskey5, key);  //继续累加
        }else {
            return true;
        }
        return false;
    }


    @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                              @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_TIMEOUT);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
