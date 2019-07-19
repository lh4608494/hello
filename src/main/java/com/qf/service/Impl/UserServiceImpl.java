package com.qf.service.Impl;

import com.qf.mapper.UserMapper;
import com.qf.pojo.User;
import com.qf.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    public Integer checkUsername(String username) {
        return userMapper.findCountByUsername(username);
    }

    public Integer register(User user) {

        //对密码加密
        //1. 对密码加密.
        String newPwd = new Md5Hash(user.getPassword(),null,1024).toString();
        user.setPassword(newPwd);
        //2. 调用mapper保存数据.
        Integer count = userMapper.save(user);
        //3. 返回信息
        return count;
    }

    public User login(String username, String password) {
        //根据用户名查询用户信息
        User user = userMapper.findByUsername(username);
        //判断查询的结果是否为null
        if (user != null){
            //判断密码
            String newPwd = new Md5Hash(password,null,1024).toString();
            //3. 如果密码正确,直接返回查询到user.
            if(user.getPassword().equals(newPwd)){
                // 登录成功,返回user对象
                return user;
            }
            //密码正确直接返回到网页查询到User
        }



        //其他情况都返回null

        return null;
    }
}
