package com.zking.demo.dao;

import com.zking.demo.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {
        User getUser(@Param("name") String name, @Param("passwrod") String passwrod);
}
