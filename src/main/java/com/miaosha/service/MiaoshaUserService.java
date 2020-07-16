package com.miaosha.service;

import com.miaosha.mapper.MiaoshaUserMapper;
import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.redis.MiaoshaUserKey;
import com.miaosha.redis.RedisService;
import com.miaosha.result.CodeMsg;
import com.miaosha.util.MD5Util;
import com.miaosha.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN="token";
    @Autowired
    private RedisService redisService;
    @Autowired
    private MiaoshaUserMapper miaoshaUserMapper;
    public String doLogin(String mobile, String inputPassword, HttpServletResponse response) {
         //先判断数据库是否有该手机号
        MiaoshaUser user= this.getUserByMobile(mobile);

        if(null==user){
            return CodeMsg.MOBILE_ISNULL.getMsg();
        }

        //获取数据库已加密两次的密码
        String dbPassword = user.getPassword();
        //将输入的密码加密两次
        String formPassToDBPass = MD5Util.formPassToDBPass(inputPassword, user.getSalt());
        //判断是否相等
          if(!dbPassword.equals(formPassToDBPass)){
              return CodeMsg.INPUT_PASSWORDERROR.getMsg();
          }

            String token= UUIDUtil.uuid();
            addCookieAndRedis(token,response,user);

        return token;
    }

    private MiaoshaUser getUserByMobile(String mobile) {
        String uuid = UUIDUtil.uuid();
        MiaoshaUser user = this.redisService.get(MiaoshaUserKey.get_userByIdKey, uuid, MiaoshaUser.class);
        if(null!=user){
            return user;
        }
        user= this.miaoshaUserMapper.getUserByMobile(mobile);
        if(null!=user){
           this.redisService.set(MiaoshaUserKey.get_userByIdKey, uuid, user);
        }
        return user;
        
    }


    //封装一个存cookie  redis的工具类
       public void addCookieAndRedis(String token,HttpServletResponse response,MiaoshaUser user){
         //保存登录信息到cookie
           //token  token就是一个key 到时候是redis里的key
           Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
           cookie.setMaxAge(MiaoshaUserKey.tokenexpireSeconds);
           cookie.setPath("/");//一定要设置到根目录
           response.addCookie(cookie);
           //user 保存登陆信息到redis
              this.redisService.set(MiaoshaUserKey.get_tokenKey,token,user);
       }

    public MiaoshaUser getUserByToken(String token, HttpServletResponse response) {
        MiaoshaUser user=this.redisService.get(MiaoshaUserKey.get_tokenKey,token,MiaoshaUser.class);
           if(null!=user){
               addCookieAndRedis(token,response,user);
           }
        return user;

    }
}
