package com.yoqin.krpc.provider.impl;

import java.util.Random;
import java.util.UUID;

import com.yoqin.krpc.api.service.UserService;
import com.yoqin.krpc.api.pojo.User;

public class UserServiceImpl implements UserService{
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了"+id+"的用户");
        //模拟数据库查询
        Random random = new Random();
        //random.nextBoolean();随机生成男或女
        User user = User.builder().userName(UUID.randomUUID().toString())
        .id(id)
        .sex(random.nextBoolean())
        .build();
        return user;
        
    }
    @Override
    public Integer insertUserId(User user) {
        System.out.println("插入数据成功："+user.getUserName());
        return user.getId();
    }
}
