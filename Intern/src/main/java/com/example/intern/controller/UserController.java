package com.example.intern.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.intern.pojo.SystemResult;
import com.example.intern.pojo.SystemResultDB;
import com.example.intern.pojo.SystemResultPage;
import com.example.intern.pojo.SystemUser;
import com.example.intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    //用户登陆
    @PostMapping(value = "/login")
    public SystemResult login(@RequestBody JSONObject a){
        String username = a.getString("username");  //从前端获取用户名
        String password = a.getString("password");
        return userService.login(username, password);}

    //检查用户是否存在+用户注册
    @PostMapping(value = "/regist")
    public SystemResult regist(@RequestBody SystemUser su){
        return userService.regist(su);
    }




}
