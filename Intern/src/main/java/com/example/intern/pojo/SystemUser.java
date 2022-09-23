package com.example.intern.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//使用lambok插件，方便快捷的实现get/set方法
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUser {
    public Integer id;
    public String username;
    public String password;
}
