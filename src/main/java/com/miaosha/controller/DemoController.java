package com.miaosha.controller;

import com.miaosha.pojo.User;
import com.miaosha.rabbit.MQSender;
import com.miaosha.redis.RedisService;
import com.miaosha.redis.UserKey;
import com.miaosha.result.CodeMsg;
import com.miaosha.result.Result;
import com.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DemoController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisService redisService;
	
	
	@Autowired
	MQSender sender;
	
	//4
	@RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> header() {
		sender.sendHeader("helloWorld");
        return Result.success("Hello，world");
    }
	//3
	@RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout() {
		sender.sendFanout("helloWorld");
        return Result.success("Hello，world");
    }
	//2
	@RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic() {
		sender.sendTopic("helloWorld");
        return Result.success("Hello，world");
    }
	//1
	@RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
		sender.send("helloWorld");
        return Result.success("Hello，world");
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/")
	@ResponseBody
	public Result<String> home() {
//		return "helloWorld";
//		return "{code:2001,msg:success,data:{name:abc}}";
		return Result.success("helloWorld");
	}
	
	@RequestMapping("/homeError")
	@ResponseBody
	public Result<CodeMsg> error() {
//		return "helloWorld";
//		return "{code:2001,msg:error,data:-1}";
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	
	@RequestMapping("/thymeleaf")
	public String thymeleaf(ModelMap map) {
		map.put("name", "wenjianying");
		return "hello";
	}
	
	@RequestMapping("/db/get/{id}")
	@ResponseBody
	public Result<String> get(@PathVariable(value="id") String id) {
		User user = this.userService.getUserById(id);
		System.out.println(user);
		return Result.success("success");
	}
	
	//测试事物是否正常
	@RequestMapping("/db/tx")
	@ResponseBody
	public Result<String> insert() {
	     Boolean flag = this.userService.insert();
		return Result.success("success");
	}
	
	@RequestMapping("/redis/set")
	@ResponseBody
	public Result<String> set() {
	    User user = new User();
	    user.setId(1);
	    user.setName("zhangsan");
	    this.redisService.set(UserKey.getById, ""+1, user);
		return Result.success("success");
	}
	@RequestMapping("/redis/get")
	@ResponseBody
	public Result<User> get() {
		User user = this.redisService.get(UserKey.getById, ""+1, User.class);
		System.out.println(user);
		
		return Result.success(user);
	}
	
}
