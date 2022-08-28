package com.example.msutils.web;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FillData {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("日期")
    private Date date;
    @ExcelProperty("年龄")
    private double number;

}
