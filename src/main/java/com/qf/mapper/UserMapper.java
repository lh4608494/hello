package com.qf.mapper;

import com.qf.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {


    //根据用户名查询数据条数
    Integer findCountByUsername(@Param("username")String username);

    Integer save(User user);

    //根据用户名查询用户信息
    User findByUsername(@Param("username")String username);
}
