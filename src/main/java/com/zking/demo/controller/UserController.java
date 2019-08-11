package com.zking.demo.controller;

import com.zking.demo.Log.face.ControllerLog;
import com.zking.demo.dao.UserService;
import com.zking.demo.mapper.UserMapper;
import com.zking.demo.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/index")
    public String Toindex(){
        return "/login";
    }
    @PostMapping("/login")
    @ControllerLog(description = "登录")
    public String login(@Param("name") String name, @Param("password") String password, HttpServletRequest request){
        User user = userService.getUser(name, password);
        System.out.println("user=========="+user);
        if(null !=user){
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            return "/index";
        }
        return  null;
    }
}
