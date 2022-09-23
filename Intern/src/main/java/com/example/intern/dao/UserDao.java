package com.example.intern.dao;

import com.example.intern.pojo.SystemDB;
import com.example.intern.pojo.SystemTable;
import com.example.intern.pojo.SystemUser;
import com.example.intern.pojo.Tableinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.List;


@Mapper //告诉springboot这是一个mybatis的mapper类
@Repository //将userDao交由spring容器管理
public interface UserDao {

    //用户登陆
    Integer login(@Param("username")String username, @Param("password")String password);
    //检测用户是否存在
    SystemUser userExist(@Param("username") String username);
    //用户具体的注册
    void regist(SystemUser su);


    //使用特定的数据库
    void useDB(@Param("dbname")String dbname);
    //用户创建数据库
    void creatDB(@Param("dbname")String dbname);
    //检测数据库是否存在
    SystemDB DBExist(@Param("dbname")String dbname);
    //将新数据库的信息写入数据库表中
    void updateDB(@Param("dbname")String dbname, @Param("note")String note, @Param("edittime")String edittime, @Param("editor")Integer editor);


    //搜索与显示数据库(显示备注名，搜索备注名)
    List<SystemDB> showDB(@Param("note") String note, @Param("editor")Integer editor,@Param("page")Integer page, @Param("pageSize")Integer pageSize);
    //获取数据库数据总量
    Long getTotal(@Param("note") String note, @Param("editor")Integer editor);

    //编辑数据库备注名
    void renameDB(@Param("id") Integer id, @Param("editor") Integer editor, @Param("note") String note);

    //删除数据库
    void dropDB(@Param("dbname") String dbname);
    //通过数据库id查找对应的数据库
    String searchDBtroughID(@Param("id") Integer id);
    //通过dbid搜索相对应的数据库表
    List<SystemTable> searchtbthroughdbid(@Param("dbid")Integer dbid);
    //通过数据库表id将Tableinfo中对应的数据删除
    void delettableinfothroughtdid(@Param("tableid")Integer tableid);
    //通过数据库id将SystemTable中对应的数据删除
    void deletsystemtablethroughdbid(@Param("dbid")Integer dbid);
    //删除数据库后，在数据库表中将所对应的数据删除
    void deletDBinfo(@Param("id") Integer id);


    //通过表给的dbid搜索对应的数据库
    SystemDB tableDB(@Param("id") Integer id);
    //展示与搜索数据库表表
    List<SystemTable> showTable(@Param("editor")Integer editor, @Param("tbname")String tbname, @Param("dbname")String dbname,@Param("page")Integer page,@Param("pageSize")Integer pageSize);
    //获取数据库biao数据总量
    Long getTableTotal(@Param("editor")Integer editor,@Param("tbname")String tbname,@Param("dbname")String dbname);

    //显示当前用户所有的数据库名
    List<String> dbname(@Param("editor") Integer editor);

    //不分页展示数据库
    List<SystemDB> noPageDB(@Param("editor") Integer editor);

    //新建一个只有一列的数据库表
    void newTable(@Param("tbname")String tbname);

    //通过传入的jsonarray所包含的数据值，一次新建一个行
    void newCol(@Param("tbname")String tbname,@Param("attricn")String attricn,@Param("attritype")String attritype);
    //通过dbname与tbname搜索tbid
    Integer searchtbid(@Param("dbname")String dbname, @Param("tbname")String tbname);
    //将新建表的列信息传入tableinfo这个表中
    void updateTableInfo(@Param("attricn")String attricn,@Param("attrien")String attrien,@Param("attriunit")String attriunit,@Param("attritype")String attritype,@Param("tableid")Integer tableid);

    //搜索在此数据库下 是否有一样的数据库表名
    SystemTable searchTable(@Param("dbname")String dbname, @Param("tbname")String tbname);

    //通过数据库名搜索对应的数据库
    SystemDB searchDBthroughDBname(@Param("dbname")String dbname);

    //建表成功后，将数据库表的信息传入对应的表中
    void updateTable(@Param("tbname")String tbname, @Param("editor")Integer editor, @Param("edittime")String edittime,@Param("dbid")Integer dbid, @Param("dbname")String dbname, @Param("note")String note);

    //通过tableid搜tableinfo这个对象
    List<Tableinfo> searchTableinfothroughID(@Param("tableid")Integer tableid);

    //通过tbid搜索tb
    SystemTable searchTb(@Param("id")Integer id);

    //删除相对应的table
     void delettable(@Param("tbname")String tbname);

     //删除SystemTable中的信息
    void deletinfofromSTB(@Param("id")Integer id);

    //删除TableIinfo中的信息
    void deletinfofromTI(@Param("tableid")Integer tableid);

    //修改数据库表名字
    void renametb(@Param("oldtb")String oldtb, @Param("newtb")String newtb);

    //修改SystemTable中的table名字
    void renametbfromST(@Param("tbname")String tbname,@Param("id")Integer id);

    //通过tableid查询所有在Tableinfo中的信息,即这个table目前有多少列
    Integer countTableinfo(@Param("tableid")Integer tableid);

}
