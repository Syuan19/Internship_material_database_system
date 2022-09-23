package com.example.intern.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.intern.dao.UserDao;
import com.example.intern.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

@Service   //  交由spring容齐管理
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    //用户登陆的业务逻辑实现
    @Override
    public SystemResult login(String username, String password) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try {
            Integer userId= userDao.login(username,password);
            //通过判断是否存在id检测是否登陆成功
            if(userId == null){
                result.setMsg("用户名或密码错误");
            }else{
                result.setMsg("登录成功");
                result.setCode(1);
                SystemUser user = new SystemUser();
                user.setId(userId);
                user.setUsername(username);
                user.setPassword(password);
                result.setData(user);

            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //用户注册的业务逻辑实现
    @Override
    public SystemResult regist(SystemUser su) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try {
            //sql语句查看用户是否存在
            SystemUser existUser = userDao.userExist(su.getUsername());
            if(existUser != null){
                //如果用户名已存在
                result.setMsg("用户名已存在");

            }else{
                //将新得到的用户信息注入数据库表中
                userDao.regist(su);
                result.setMsg("注册成功");
                result.setCode(1);
                result.setData(su);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //用户新建一个数据库
    @Override
    public SystemResult creatDB(String dbname, String note, Integer editor) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            //检测数据库之前知否存在
            SystemDB existDB = userDao.DBExist(dbname);
            if(existDB != null){
                //如果数据库已存在
                result.setMsg("数据库已存在,请输入新的数据库名");
            }else{

                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");

                //将新的数据库信息写入数据库表
                userDao.updateDB(dbname, note, dateFormat.format(date), editor);
                //创建新的数据库
                userDao.creatDB(dbname);
                result.setMsg("数据库创建成功");
                result.setCode(1);

                SystemDB DB = new SystemDB();
                DB.setDbname(dbname);
                DB.setNote(note);
                DB.setEdittime(dateFormat.format(date));
                DB.setEditor(editor);
                result.setData(DB);
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //搜索与显示用户目前所拥有的数据库
    @Transactional
    @Override
    public SystemResultPage showDB(String note,Integer editor, Integer page, Integer pageSize) {
        SystemResultPage result = new SystemResultPage();
        result.setCode(0);
        result.setData(null);
        try{
            if (page != null && pageSize != null) {
                page = (page-1)*pageSize;
            }
            //获取当前用户所拥有的数据库
            List<SystemDB> DBlist = userDao.showDB(note, editor,page,pageSize);
            if(DBlist == null){
                //如果没有搜到任何数据库
                result.setMsg("未找到数据库");
            }else{
                //获取数据总量
                Long total = userDao.getTotal(note,editor);
                result.setCode(1);
                result.setMsg("检索成功");
                result.setData(DBlist);
                result.setTotal(total);
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    //修改数据库显示的备注名
    @Override
    public SystemResult renameDB(String note, Integer id, Integer editor) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            userDao.renameDB(editor,id,note);
            result.setMsg("修改成功");
            result.setCode(1);
            result.setData(note);
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;

    }
    //删除想要删除的数据库
    @Override
    public SystemResult deletDB(Integer id) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            //先通过前端返回的数据库id找到所对应的数据库名
            String dbname=userDao.searchDBtroughID(id);
            //通过传入的dbid将查询相对应的数据库表
            List<SystemTable> list1 = userDao.searchtbthroughdbid(id);
            //对每一张表进行迭代
            ListIterator<SystemTable> it = list1.listIterator();
            while (it.hasNext()) {
                //获取SystemTable对象
                SystemTable item = it.next();
                //获取数据库表id
                Integer tableid = item.getId();
                //通过数据库表id删除Tableinfo中所有相关的数据
                userDao.delettableinfothroughtdid(tableid);
            }

            //删除Systemtable中所有相关的数据
            userDao.deletsystemtablethroughdbid(id);

            //再使用sql语句将数据库删除
            userDao.dropDB(dbname);
            //再将数据库表中的数据删除

            result.setMsg("删除成功");
            result.setCode(1);
            //最后在数据库表中删除相关信息
            userDao.deletDBinfo(id);

        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //显示当前用户所拥有的数据库表
    @Transactional
    @Override
    public SystemResultPage showTable(Integer editor, Integer id, String dbname, String tbname,Integer page, Integer pageSize) {
        SystemResultPage result = new SystemResultPage();
        result.setCode(0);
        result.setData(null);
        try{
            //分页操作
            if (page != null && pageSize != null) {
                page = (page-1)*pageSize;
            }

            //使用sql语句搜索当前用户所拥有的数据库表
            //搜索systemTable这张表
            List<SystemTable> list1 = userDao.showTable(editor,tbname,dbname,page,pageSize);
            if(list1 != null){
                //获取数据总量
                Long total = userDao.getTableTotal(editor,tbname,dbname);

                //建立一个array 之后的数据都放到这里面
                JSONObject[] ar1 = new JSONObject[list1.size()];
                int a = 0;
                //遍历这个list--每次获得一个list中的表对象
                ListIterator<SystemTable> it = list1.listIterator();
                while (it.hasNext()) {
                    //创建一个JSONObject用于存放三个对象
                    JSONObject re = new JSONObject();

                    //array中的第一个对象tb ----- 获得每个list的table对象
                    SystemTable item = it.next();

                    //array中的第二个对象db -----从这个item中获得他所属的db的name消息
                    String db_name = item.getDbname();
                    //通过dbname获得一个数据库对象
                    SystemDB db = userDao.searchDBthroughDBname(db_name);

                    //array中的第三个对象Tableinfo ---从item中获得tableid
                    Integer tableid = item.getId();
                    //通过tableid搜索Tableinfo
                    List<Tableinfo> tbinfo = userDao.searchTableinfothroughID(tableid);

                    //re中存放数据
                    re.put("SystemTable",item);
                    re.put("SystemDB",db);
                    re.put("tableinfo",tbinfo);

                    //将每遍历一次的后的array放入相应的re
                    ar1[a] = re;
                    a++;

                }

                //放入相应的数据
                result.setTotal(total);
                result.setCode(1);
                result.setMsg("搜索成功");
                result.setData(ar1);

            }else{
                result.setCode(0);
                result.setMsg("未找到数据");
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //当前用户所拥有的所有数据库名
    @Override
    public SystemResult dbname(Integer editor) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            //先通过前端返回的数据库id找到所对应的数据库名
            List<String> dbname=userDao.dbname(editor);
            if(dbname ==null){
                result.setMsg("未找到相关数据库");
            }else{
                result.setCode(1);
                result.setData(dbname);
                result.setMsg("查找成功");
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //不分页展示数据库
    @Override
    public SystemResult noPageDB(Integer editor) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            //再使用sql语句搜索当前用户所拥有的数据库表
            List<SystemDB> list1 = userDao.noPageDB(editor);
            if(list1 != null){
                result.setCode(1);
                result.setMsg("搜索成功");
                result.setData(list1);
            }else{
                result.setCode(0);
                result.setMsg("未找到数据");
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //选定数据库 + 写数据库表名字 + 传入每一列的信息 = 新建一个表
    @Override
    public SystemResult newTable(String tbname, Integer dbId, Integer editor, JSONArray attri) {
        JSONObject re = new JSONObject();
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            //使用intern这个数据库
            String i = "intern";
            userDao.useDB(i);
            //通过id查找数据库的名字
            String dbname = userDao.searchDBtroughID(dbId);
            //搜索是否在此数据库中有一样的数据库表名，如果重复则不能创建
            SystemTable tb = userDao.searchTable(dbname,tbname);

            if(tb!=null){
                result.setCode(0);
                result.setMsg("创建失败，已存在同名表");
            }else{
                //使用用户选中的数据库
                userDao.useDB(dbname);
                //在此数据库下创建数据库表
                userDao.newTable(tbname);
                //获取此表创建的时间
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");

                userDao.useDB(i);
                //获得此数据库的信息-----要被写入JSONObject中的数据库信息
                SystemDB db = userDao.searchDBthroughDBname(dbname);
                String note =db.getNote();
                //将创建的数据库表信息写入SystemTable这张表中-----要被写入JSONObject的数据库表信息
                userDao.updateTable(tbname,editor,dateFormat.format(date),dbId, dbname,note);

                //返回新创建的数据库表对象
                SystemTable tableData = new SystemTable();
                tableData.setTbname(tbname);
                tableData.setEdittime(dateFormat.format(date));
                tableData.setEditor(editor);
                tableData.setDbname(dbname);
                tableData.setDbid(dbId);
                tableData.setNote(note);

                //获得从array中传入的attriname和type
                for(int j=0;j<attri.size();j++){

                    userDao.useDB(dbname);
                    JSONObject temp = attri.getJSONObject(j);
                    String attricn = temp.getString("attricn");
                    String attrien = temp.getString("attrien");
                    String attriunit = temp.getString("attriunit");
                    String attritype = temp.getString("attritype");
                    //使用intern这个系统主数据库，并从Tableinfo这个表中查看是否已经有一样的列名--通过dbname与tbname与attri
                    //后续发现不用，因为是一个新的表

                    //若没有相同的表名则跳转至目前的数据库并新建列
                    userDao.newCol(tbname,attricn,attritype);
                    //将新建的列信息传入主系统库intern中的Tableinfo这个表
                    userDao.useDB(i);
                    //第一步：通过连接至SystemTable这个表，获取tableid===通过dbname与tbname搜
                    Integer tableid = userDao.searchtbid(dbname,tbname);
                    //第二步：将现有的所有数据填入TableInfo这个表
                    userDao.updateTableInfo(attricn,attrien,attriunit,attritype,tableid);
                }
                //数据库对象db--写入re这个JSONObject中
                re.put("dbinfo",db);
                re.put("tableinfo",tableData);
                re.put("colInfo",attri);

                //将最后返回的值当做一个array 再通过restcontroller返回给前端
                JSONObject[] ar1 = new JSONObject[]{re};
                result.setData(ar1);

                //返回创建状态
                result.setCode(1);
                result.setMsg("创建成功");
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //删除选定的数据库表
    @Override
    public SystemResult dropTable(Integer id) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            //通过传入的表id获得相对应的数据库表
            SystemTable tb = userDao.searchTb(id);
            String tbname = tb.getTbname();
            String dbname = tb.getDbname();

            //先将intern库中Tableinfo这张表的相关信息删除
            //再将intern库中SystemTable这张表的相关信息删除
            userDao.useDB("intern");
            userDao.deletinfofromTI(id);
            userDao.deletinfofromSTB(id);

            //在使用表所对应的数据库
            userDao.useDB(dbname);
            //再使用sql语句将数据库表删除
            userDao.delettable(tbname);

            result.setMsg("删除成功");
            result.setCode(1);

        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //修改数据库的表名字
    @Override
    public SystemResult renametb(String newtb, Integer id,JSONArray attri) {
        SystemResult result = new SystemResult();
        result.setCode(0);
        result.setData(null);
        try{
            SystemTable tb = userDao.searchTb(id);

            //通过tb获得相对应的数据库表名，即oldtb
            String oldtb = tb.getTbname();
            String dbname = tb.getDbname();
            userDao.useDB(dbname);
            //修改tb的name
            userDao.renametb(oldtb,newtb);

            //修改SystemTable中的表信息
            userDao.useDB("intern");
            userDao.renametbfromST(newtb,id);

            //-------------以上是修改数据库表名的操作------------------//
            Integer total = userDao.countTableinfo(id);
            //获得从array中传入的attriname和type
            for(int j=total;j<attri.size();j++){

                userDao.useDB(dbname);
                JSONObject temp = attri.getJSONObject(j);
                String attricn = temp.getString("attricn");
                String attrien = temp.getString("attrien");
                String attriunit = temp.getString("attriunit");
                String attritype = temp.getString("attritype");

                //具体创建新列的操作
                userDao.newCol(newtb,attricn,attritype);
                //将新建的列信息传入主系统库intern中的Tableinfo这个表
                userDao.useDB("intern");
                //第二步：将现有的所有数据填入TableInfo这个表
                userDao.updateTableInfo(attricn,attrien,attriunit,attritype,id);


            }

            JSONObject re = new JSONObject();
            re.put("newTableName",newtb);
            re.put("tableinfo",attri);


            result.setMsg("修改成功");
            result.setCode(1);
            result.setData(re);
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


}
