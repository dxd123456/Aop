package com.zking.demo.dao.impl;

import com.zking.demo.Log.face.ServiceLog;
import com.zking.demo.dao.UserService;
import com.zking.demo.mapper.UserMapper;
import com.zking.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {
   @Autowired
   private UserMapper userMapper;
    @Override
    @ServiceLog(description = "登录用户")
    public User getUser(String name, String passwrod) {
        return userMapper.getUser(name,passwrod);
    }
}
