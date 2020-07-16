package com.miaosha.controller;

import com.miaosha.result.CodeMsg;
import com.miaosha.result.Result;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
     @RequestMapping("/do_login")
     @ResponseBody
    public Result<String> doLogin(String mobile, String password, HttpServletResponse response){
         System.out.println("4444444444");
         System.out.println(mobile);
         System.out.println(password);
                //判断手机号是否为空   手机号的格式
            if(StringUtils.isEmpty(mobile)){
                  return Result.error(CodeMsg.INPUT_MOBILE_ISNULL);
            }

            if(!ValidatorUtil.isMobile(mobile)){
                return Result.error(CodeMsg.MOBILE_ISERROR);
            }

            //判断密码是否为空
             if(StringUtils.isEmpty(password)){
                 return Result.error(CodeMsg.PASSWORD_ISNULL);
             }



             //关于登录   登录先不返回对象的信息  先返回正确的数据
                String token=this.miaoshaUserService.doLogin(mobile,password,response);
            /* if(codeMsg.getCode()==0){
                 return Result.success(true);
             }else{
                 return Result.error(codeMsg);
             }*/
         return Result.success(token);

    }
}
