package com.example.intern.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDB {
    public String dbname;
    public Integer id;
    public Integer editor;
    public String edittime;
    public String note;
}
