package com.zking.demo.mapper;

import com.zking.demo.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service("userMapper")
public interface UserMapper {
    User getUser(@Param("name") String name,@Param("passwrod") String passwrod);
}
