package com.example.intern.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



//本对象用于分页返回的数据
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemResultPage<T> {
    //给用户返回的消息
    private String msg;
    //数据是否正常请求
    private Integer code;
    //返回所有的数据量total
    private Long total;
    //具体返回的数据
    private T data;
}