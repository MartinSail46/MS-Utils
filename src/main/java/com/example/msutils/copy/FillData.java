package com.example.msutils.copy;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class FillData {

    @ExcelProperty("开发模块")
    private String module;

    @ExcelProperty("功能点")
    private String functionPoint;

    @ExcelProperty("代码路径")
    private String codePath;

    @ExcelProperty("操作")
    private String operation;

    @ExcelProperty("备注")
    private String remark;

}
