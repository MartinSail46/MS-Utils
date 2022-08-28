package com.example.msutils.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * EasyExcel导出工具类
 *
 * @author yffu
 */
@Component
public class EasyExcelWriter {

    @Value("${path.easyExcel.template}")
    private String templatePath;//模板文件所在路径
    @Value("${path.easyExcel.exportFile}")
    private String filePath;//导出文件所在路径


    /**
     * 模板的填充导出
     *
     * @param templateFileName 模板文件名
     * @param exportData       模板填充数据
     * @return String 导出的文件路径
     */
    public String sheetsWrite(String templateFileName, ExportData... exportData) {
        return this.sheetsWrite(templateFileName, Boolean.FALSE, exportData);
    }


    /**
     * 复杂模板的填充
     * 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。
     * forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
     * 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
     * 如果数据量大 list不是最后一行 参照下一个
     *
     * @param templateFileName 模板文件名
     * @param forceNewRow      list是否为最后一行（默认false）
     * @param exportData       模板填充数据
     * @return String 导出的文件路径
     */
    public String sheetsWrite(String templateFileName, Boolean forceNewRow, ExportData... exportData) {
        String fileName = filePath + System.currentTimeMillis() + ".xlsx";//生成随机文件名

        try (ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templatePath + templateFileName).build()) {
            for (int i = 0; i < exportData.length; i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet().sheetNo(i).build();
                FillConfig fillConfig = FillConfig.builder().forceNewRow(forceNewRow).build();
                excelWriter.fill(exportData[i].getBodyData(), fillConfig, writeSheet);//填充List数据
                excelWriter.fill(exportData[i].getHeaderData(), writeSheet);//填充头部数据
            }
        }
        return fileName;
    }


    @Test
    @GetMapping("/test")
    public void entry() {
        //Sheet2
        ExportData exportData = new ExportData();
        Map<String, Object> map = MapUtils.newHashMap();
        map.put("title", "周报测试");
        map.put("startTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        map.put("endTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        map.put("orgId", 1000);

        ArrayList<FillData> fillData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FillData data = new FillData();
            data.setName("张三" + i);
            data.setNumber(new Random().nextInt());
            data.setDate(new Date());
            fillData.add(data);
        }

        exportData.setHeaderData(map);
        exportData.setBodyData(fillData);

        //Sheet2
        ExportData exportData2 = new ExportData();
        Map<String, Object> map2 = MapUtils.newHashMap();
        map2.put("title", "周报测试2");
        map2.put("startTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        map2.put("endTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        map2.put("orgId", 1000);

        ArrayList<FillData> fillData2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FillData data = new FillData();
            data.setName("张三" + i);
            data.setNumber(new Random().nextInt());
            data.setDate(new Date());
            fillData2.add(data);
        }

        exportData2.setHeaderData(map2);
        exportData2.setBodyData(fillData2);
//        this.complexFill(map, fillData, "template.xlsx");
        this.sheetsWrite("template.xlsx", exportData, exportData2);
    }
}
