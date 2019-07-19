package com.qf.service;

import com.qf.AcTest;
import com.qf.mapper.UserMapper;
import com.qf.pojo.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

public class UserServiceTest extends AcTest {

    @Autowired
    private UserService userService;
    @Test
    public void checkUsername() {
    }

    @Test
    @Transactional
    public void register() {
        User user = new User();
        user.setUsername("xxxx");
        user.setPassword("xxxx");
        user.setPhone("11111111111");

        Integer count = userService.register(user);

        System.out.println(count);

    }

    @Test
    public void login(){
        User login = userService.login("admin","admin123");
        System.out.println(new Md5Hash("admin",null,1024).toString());
        System.out.println(login);
    }
}