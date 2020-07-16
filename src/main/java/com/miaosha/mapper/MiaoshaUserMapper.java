package com.miaosha.mapper;

import com.miaosha.pojo.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface MiaoshaUserMapper {
    @Select("select * from miaosha_user where id=#{mobile}")
    MiaoshaUser getUserByMobile(String mobile);
}
