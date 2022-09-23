package com.example.intern.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemResult<T> {
    //给用户返回的消息
    private String msg;
    //数据是否正常请求
    private Integer code;
    //具体返回的数据
    private T data;
}