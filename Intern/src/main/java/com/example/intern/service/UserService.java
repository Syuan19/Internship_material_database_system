package com.example.intern.service;

import com.alibaba.fastjson.JSONArray;
import com.example.intern.pojo.SystemResult;
import com.example.intern.pojo.SystemResultDB;
import com.example.intern.pojo.SystemResultPage;
import com.example.intern.pojo.SystemUser;


public interface UserService {

    //用户登陆
    public SystemResult login(String username, String password);
    //用户存在+用户注册
    public SystemResult regist(SystemUser su);
    //用户创建新的数据库
    public SystemResult creatDB(String dbname, String note, Integer editor);

    //搜索与显示目前现在所拥有的数据库
    public SystemResultPage showDB(String note, Integer editor, Integer page, Integer pageSize);

    //修改数据库的备注名
    public SystemResult renameDB(String note, Integer id, Integer editor);

    //删除数据库
    public SystemResult deletDB(Integer id);

    //展示当前用户所拥有的数据库表表
    public SystemResultPage showTable(Integer editor, Integer id, String dbname, String tbname,Integer page,Integer pageSize);

    //显示当前用户所有的数据库名字
    public SystemResult dbname(Integer editor);

    //不分页展示数据库
    public SystemResult noPageDB(Integer editor);

    //选定数据库 + 写数据库表名字 = 新建一个表
    public SystemResult newTable(String tbname, Integer dbId, Integer editor, JSONArray attri);

    //删除一张数据库表
    public SystemResult dropTable(Integer id);

    //更名一张数据库表
    public SystemResult renametb(String newtb,Integer id,JSONArray attri);

}
