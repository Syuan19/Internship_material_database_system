package com.example.intern.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.intern.pojo.SystemResult;
import com.example.intern.pojo.SystemResultPage;
import com.example.intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DBController {

    @Autowired
    private UserService userService;

    //用户创建新的数据库
    @PostMapping(value = "/creatDB")
    public SystemResult creatDB(@RequestBody JSONObject a, HttpServletRequest request){
        //从前端获取三个相关的参数
        String dbname =a.getString("dbname");
        String note = a.getString("note");
        Integer editor = Integer.valueOf(request.getHeader("token"));
        return userService.creatDB(dbname, note,editor);
    }

    //显示用户目前所拥有的数据库+搜索相关数据库
    @PostMapping(value="/showDB")
    public SystemResultPage showDB(@RequestBody JSONObject a, HttpServletRequest request){
        //获得相关的参数
        String note = a.getString("note");
        Integer page = a.getInteger("page");
        Integer pageSize = a.getInteger("pageSize");
        Integer editor = Integer.valueOf(request.getHeader("token"));
        System.out.println(editor);
        return userService.showDB(note,editor,page,pageSize);
    }

    //修改当前用户想要修改的数据库备注名（本项目中，页面显示的是数据库备注名，而非数据库名）
    @PostMapping(value="/renameDB")
    public SystemResult renameDB(@RequestBody JSONObject a, HttpServletRequest request){
        String note = a.getString("note");
        Integer id = a.getInteger("id");
        Integer editor = Integer.valueOf(request.getHeader("token"));
        return userService.renameDB(note,editor,id);
    }

    //删除用户想要删除的数据库
    @PostMapping(value="/deletDB")
    public SystemResult deletDB(@RequestBody JSONObject a,HttpServletRequest request){
        Integer id = a.getInteger("id");
        System.out.println(id);
        Integer editor = Integer.valueOf(request.getHeader("token"));
        return userService.deletDB(id);
    }

}
