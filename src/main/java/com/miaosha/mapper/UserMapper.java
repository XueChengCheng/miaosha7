package com.miaosha.mapper;

import com.miaosha.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

	@Select("select * from user where id=#{id}")
	User getUserById(String id);
	
    @Insert("insert into user(id,name) values(#{id},#{name})")
	void insert(User user);

}
