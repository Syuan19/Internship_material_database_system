package com.example.intern.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
//每张表都需要返还给前端一个信息包括下面所有
public class SystemTable {
    //与数据库表有关的信息
    public Integer id;
    public String tbname;
    public String edittime;
    public Integer editor;

    //对象中与数据库有关的信息
    public String dbname;
    public Integer dbid;
    public String note;

}
