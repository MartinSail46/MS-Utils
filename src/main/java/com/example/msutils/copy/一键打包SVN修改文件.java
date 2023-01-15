package com.example.msutils.copy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.io.File;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class 一键打包SVN修改文件 {
    //前端工程所在路径
    private static final String FRONTEND = "/Users/martinsail/workspace/AmarirsV4.0/AmarIRSV4.0-CDBK/IRS/irs-web";
    //前端Excel文档所在路径
    private static final String FRONTEND_EXCELPATH = "/Users/martinsail/Desktop/test.xlsx";
    //后端工程所在路径
    private static final String BACKEND = "/Users/martinsail/workspace/AmarirsV4.0/AmarIRSV4.0-CDBK/IRS/irs-server";
    //后端Excel文档所在路径
    private static final String BACKEND_EXCELPATH = "/Users/martinsail/Desktop/test.xlsx";
    //文件输出路径
    private static final String OUTPUT_PATH = "/Users/martinsail/Desktop/AmarIRSV4.0/";

    public static void main(String[] args) {
        一键打包SVN修改文件 一键打包SVN修改文件 = new 一键打包SVN修改文件();
        一键打包SVN修改文件.前端文件拷贝();
//        一键打包SVN修改文件.后端文件拷贝();
    }


    public void 前端文件拷贝() {
        ExcelUtil.getReader(FRONTEND_EXCELPATH).readAll().stream().forEach(map -> {
            FileUtil.copyFile(FRONTEND + File.separator + map.get("文件路径"), OUTPUT_PATH + File.separator + map.get("文件路径"), REPLACE_EXISTING);
            FileUtil.copyFile(FRONTEND_EXCELPATH,OUTPUT_PATH+ StringUtils.getFilename(FRONTEND_EXCELPATH),REPLACE_EXISTING);
        });
    }

    public void 后端文件拷贝() {
        ExcelUtil.getReader(BACKEND_EXCELPATH).readAll().stream().forEach(map -> {
            FileUtil.copyFile(BACKEND + File.separator + map.get("文件路径"), OUTPUT_PATH + File.separator + map.get("文件路径"), REPLACE_EXISTING);
            FileUtil.copyFile(BACKEND_EXCELPATH,OUTPUT_PATH+ StringUtils.getFilename(BACKEND_EXCELPATH),REPLACE_EXISTING);
        });
    }
}
