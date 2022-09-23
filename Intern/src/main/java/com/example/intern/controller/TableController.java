package com.example.intern.controller;

import com.alibaba.fastjson.JSONArray;
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
public class TableController {
    @Autowired
    private UserService userService;

    //搜索当前用户所拥有的数据库表表
    @PostMapping(value="/showTable")
    public SystemResultPage showTable(@RequestBody JSONObject a, HttpServletRequest request){
        String dbname = a.getString("dbname");
        String tbname = a.getString("tbname");
        Integer page = a.getInteger("page");
        Integer pageSize = a.getInteger("pageSize");
        Integer id = a.getInteger("id");
        Integer editor = Integer.valueOf(request.getHeader("token"));
        return userService.showTable(editor,id,dbname, tbname,page,pageSize);
    }

    //不分页展示数据库----用于下拉框的数据库姓名展示
    @GetMapping(value="/noPageDB")
    @CrossOrigin
    public SystemResult noPageDB(HttpServletRequest request){
        Integer editor = Integer.valueOf(request.getHeader("token"));
        return userService.noPageDB(editor);
    }

    //在选中的数据库下创建新的数据库表
    @PostMapping(value="newTable")
    @CrossOrigin
    public SystemResult newTable(@RequestBody JSONObject a,HttpServletRequest request){
        Integer dbId = a.getInteger("dbid");
        String tbname = a.getString("tbname");
        JSONArray attri = a.getJSONArray("array");
        Integer editor = Integer.valueOf(request.getHeader("token"));

        return userService.newTable(tbname,dbId,editor,attri);
    }

    //删除选中的数据库表
    @PostMapping(value="delettable")
    @CrossOrigin
    public SystemResult delettable(@RequestBody JSONObject a, HttpServletRequest request){
        Integer editor = Integer.valueOf(request.getHeader("token"));
        Integer id = a.getInteger("tbid");
        return userService.dropTable(id);
    }

    //修改数据库表名字
    @PostMapping(value="renametb")
    @CrossOrigin
    public SystemResult renametb(@RequestBody JSONObject a, HttpServletRequest request){
        String newtb = a.getString("tbname");
        Integer id = a.getInteger("tbid");
        JSONArray attri = a.getJSONArray("array");
        return userService.renametb(newtb,id,attri);
    }

}
