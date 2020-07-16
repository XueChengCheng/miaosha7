package com.miaosha.controller;

import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.redis.GoodsKey;
import com.miaosha.redis.RedisService;
import com.miaosha.result.CodeMsg;
import com.miaosha.result.Result;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.vo.GoodsDetailVo;
import com.miaosha.vo.GoodsVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xueyaru
 * 商品控制器
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private RedisService redisService;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

      @Autowired
      private MiaoshaUserService miaoshaUserService;

      @Autowired
      private GoodsService goodsService;
  /*   @RequestMapping("/to_list")
    public String toList(@CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false)String cookieToken,
                         @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false)String requestToken,
                         Model model){
   if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(requestToken)){
       System.out.println("ssssssssssssss"+requestToken+cookieToken);
       return "login";

   }
         String token=StringUtils.isEmpty(cookieToken)?requestToken:cookieToken;
         MiaoshaUser user=this.miaoshaUserService.getUserByToken(token);
         model.addAttribute("user",user);
     return "goods_list";
    }*/
//商品列表页面
    int i=1;
    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toList(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response){
        System.out.println("222222222********"+i++);
          if(user==null){
               return "login";
          }
          //从缓存里面获取页面数据
        String html=this.redisService.get(GoodsKey.getGoodsListKey,"",String.class);
          //如果有数据的话直接返回
          if(!StringUtils.isEmpty(html)){
              return html;
          }

          //没有数据
          //查询商品数据
        List<GoodsVo> goodsList=this.goodsService.queryGoods();
          model.addAttribute("goodsList",goodsList);

          //将查询出来的渲染为HTML字符串
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                                                     request.getLocale(), model.asMap(), applicationContext);
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        //如果html不为空，把html保存到redis里面，时间为60秒
         if(!StringUtils.isEmpty(html)){
             this.redisService.set(GoodsKey.getGoodsListKey,"",html);
         }

        return html;
    }


   /* //商品详情页面
    @RequestMapping(value = "/to_detail/{goodsId}")
    public String toDetail(Model model, MiaoshaUser user, @PathVariable(value = "goodsId")Long goodsId){
          if(user==null){
               return "login";
          }
        // 提示：四个参数：user、goods、秒杀状态、剩余时间（毫秒）
          GoodsVo goodsVo=this.goodsService.queryGoodsByGoodSId(goodsId);
        System.out.println("商品详情"+goodsVo);
           int miaoshaStatus=0;// 0秒杀倒计时 1秒杀进行中 2秒杀已结束

        //根据时间判断秒杀状态
         long start=goodsVo.getStartDate().getTime();//开始时间
          long end=goodsVo.getEndDate().getTime();//结束时间
         long now=System.currentTimeMillis();//当前时间
         long remainSeconds=0;//据秒杀开始的剩余时间
        if(now<start){
            //未开始
            miaoshaStatus=0;
            remainSeconds=(start-now)/1000;
        }else if(now > end){
            //结束
              miaoshaStatus=2;
              remainSeconds=-1;

        }else {
            //进行中
            miaoshaStatus=1;
            remainSeconds=0;
        }



          model.addAttribute("user",user);
          model.addAttribute("goods",goodsVo);
          model.addAttribute("miaoshaStatus",miaoshaStatus);
          model.addAttribute("remainSeconds",remainSeconds);
     return "goods_detail";
    }
    */

    //商品详情页面优化
/*    @RequestMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetail(Model model, MiaoshaUser user, @PathVariable(value = "goodsId")Long goodsId,
            HttpServletRequest request, HttpServletResponse response){
          if(user==null){
               return "login";
          }

          String html=this.redisService.get(GoodsKey.getGoodsDetailKey,"",String.class);
          if(!StringUtils.isEmpty(html)){
                  return html;
          }
        // 提示：四个参数：user、goods、秒杀状态、剩余时间（毫秒）
          GoodsVo goodsVo=this.goodsService.queryGoodsByGoodSId(goodsId);
        System.out.println("商品详情"+goodsVo);
           int miaoshaStatus=0;// 0秒杀倒计时 1秒杀进行中 2秒杀已结束

        //根据时间判断秒杀状态
         long start=goodsVo.getStartDate().getTime();//开始时间
          long end=goodsVo.getEndDate().getTime();//结束时间
         long now=System.currentTimeMillis();//当前时间
         long remainSeconds=0;//据秒杀开始的剩余时间
        if(now<start){
            //未开始
            miaoshaStatus=0;
            remainSeconds=(start-now)/1000;
        }else if(now > end){
            //结束
              miaoshaStatus=2;
              remainSeconds=-1;

        }else {
            //进行中
            miaoshaStatus=1;
            remainSeconds=0;
        }



          model.addAttribute("user",user);
          model.addAttribute("goods",goodsVo);
          model.addAttribute("miaoshaStatus",miaoshaStatus);
          model.addAttribute("remainSeconds",remainSeconds);

        //将查询出来的渲染为HTML字符串
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        //如果html不为空，把html保存到redis里面，时间为60秒
        if(!StringUtils.isEmpty(html)){
            this.redisService.set(GoodsKey.getGoodsDetailKey,"",html);
        }

        return html;

    } */


    //商品详情页面优化  静态化  动静分离
    @RequestMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(Model model, MiaoshaUser user, @PathVariable(value = "goodsId")Long goodsId){
          if(user==null){
               return Result.error(CodeMsg.SESSION_TIMEOUT);
          }


        // 提示：四个参数：user、goods、秒杀状态、剩余时间（毫秒）
          GoodsVo goodsVo=this.goodsService.queryGoodsByGoodSId(goodsId);
          System.out.println("商品详情"+goodsVo);
           int miaoshaStatus=0;// 0秒杀倒计时 1秒杀进行中 2秒杀已结束

        //根据时间判断秒杀状态
          long start=goodsVo.getStartDate().getTime();//开始时间
          long end=goodsVo.getEndDate().getTime();//结束时间
          long now=System.currentTimeMillis();//当前时间
          long remainSeconds=0;//据秒杀开始的剩余时间
          if(now<start){
            //未开始
            miaoshaStatus=0;
            remainSeconds=(start-now)/1000;
         }else if(now > end){
            //结束
              miaoshaStatus=2;
              remainSeconds=-1;

         }else {
            //进行中
            miaoshaStatus=1;
            remainSeconds=0;
         }


        GoodsDetailVo goodsDetailVo = new GoodsDetailVo(miaoshaStatus, remainSeconds, goodsVo, user);


        return Result.success(goodsDetailVo);

    }
}
