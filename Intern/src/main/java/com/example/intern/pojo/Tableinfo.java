package com.example.intern.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//表每个字段 都应成为一个对象
public class Tableinfo {
    //对应那一张表
    public String tableid;
    //字段的中文
    public String attricn;
    //字段的英文
    public String attrien;
    //字段的单位
    public String attriunit;
    //字段的类型
    public String attritype;
}
