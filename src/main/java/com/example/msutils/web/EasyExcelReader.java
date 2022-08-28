package com.example.msutils.web;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EasyExcelReader {


    @Value("${path.easyExcel.template}")
    private String templatePath;//模板文件所在路径
    @Value("${path.easyExcel.exportFile}")
    private String filePath;//导出文件所在路径

    //表头数据
    public static List<Map<Integer, String>> HEADERLIST = new ArrayList<>();

    /**
     * 多个Sheet页读取
     *
     * @param fileName 导入文件名
     * @param clazz    多个数据填充对象，按Sheet页顺序填写
     * @return List<ImportData> 读取的数据列表
     */
    public List<ImportData> sheetsRead(String fileName, Class<?>... clazz) {
        return this.sheetsRead(fileName, 1, clazz);
    }

    /**
     * 多个Sheet页读取，自定义表头长度相同
     *
     * @param fileName      导入文件名
     * @param headRowNumber 表头长度
     * @param clazz         多个数据填充对象，按Sheet页顺序填写
     * @return List<ImportData> 读取的数据列表
     */
    public List<ImportData> sheetsRead(String fileName, Integer headRowNumber, Class<?>... clazz) {
        ArrayList<Integer> headRowNumbers = new ArrayList<>();
        for (int i = 0; i < clazz.length; i++) {
            headRowNumbers.add(headRowNumber);
        }
        return this.sheetsRead(fileName, headRowNumbers, clazz);
    }

    /**
     * 多个Sheet页读取，自定义表头长度不同
     *
     * @param headRowNumbers 表头长度数组
     * @param fileName       导入文件名
     * @param clazz          多个数据填充对象，按Sheet页顺序填写
     * @return List<ImportData> 读取的数据列表
     */
    public List<ImportData> sheetsRead(String fileName, List<Integer> headRowNumbers, Class<?>... clazz) {
        //创建返回体
        ArrayList<ImportData> rsp = new ArrayList<>();

        //数据列表
        if (headRowNumbers.size() != clazz.length) {
            log.error("表头长度数组与sheet页数量不一致");
            return null;
        }
        for (int i = 0; i < clazz.length; i++) {
            ImportData importData = new ImportData();
            //清空列表,不能进行clear(),因为是浅拷贝
            ArrayList<Object> list = new ArrayList<>();
            Map<Integer, List<?>> map = new HashMap<>();
            HEADERLIST = new ArrayList<>();

            //读取Excel
            EasyExcel.read(templatePath + fileName, clazz[i], new SheetPageReadListener<>(list::add)).sheet(i).headRowNumber(headRowNumbers.get(i)).doRead();
            importData.setHeaderData(HEADERLIST);//表头数据
            importData.setBodyData(list);//表格数据
            rsp.add(importData);
        }
        return rsp;
    }


    /**
     * 不创建对象的读
     */
    @Test
    public void noModelRead() {
        List<ImportData> importData = sheetsRead("1661687960012.xlsx", 5, FillData.class, FillData.class);
        for (int i = 0; i < importData.size(); i++) {
            log.info("读取到一条数据{}", importData.get(i).toString());
        }
    }
}
