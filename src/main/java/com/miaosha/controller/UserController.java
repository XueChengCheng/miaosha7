package com.miaosha.controller;

import com.miaosha.pojo.MiaoshaUser;
import com.miaosha.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserController {

	/**
	 * qps: 450  ,测试10000次访问
	 * @param miaoshaUser
	 * @return
	 */
   @RequestMapping("/user_info")
   @ResponseBody
   public Result<MiaoshaUser> list(MiaoshaUser miaoshaUser ) {  //数据来自redis
	   System.out.println("**"+miaoshaUser);
	   return Result.success(miaoshaUser);
   }
  
	 	
}
