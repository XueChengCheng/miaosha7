package com.miaosha.service;

import com.miaosha.mapper.UserMapper;
import com.miaosha.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class UserService {

	@Autowired
	private UserMapper userMapper;
	public User getUserById(String id) {
		User user = this.userMapper.getUserById(id);
		return user;
	}
	
	@Transactional
	public Boolean insert() {
		
		User user = new User();
		user.setId(4);
		user.setName("lisi");
		this.userMapper.insert(user);
		
		user.setId(5);
		user.setName("wangwu");
		this.userMapper.insert(user);
		
		return null;
	}

}
