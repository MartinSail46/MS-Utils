package com.example.msutils.copy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import org.springframework.core.io.ClassPathResource;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SVNLOG {
    private static final SVNClientManager manager;

    //前端工程所在路径
    private static final String FRONTEND = "/Users/martinsail/workspace/AmarirsV4.0/AmarIRSV4.0-CDBK/IRS/irs-web";
    //后端工程所在路径
    private static final String BACKEND = "/Users/martinsail/workspace/AmarirsV4.0/AmarIRSV4.0-CDBK/IRS/irs-server";
    //文件输出路径
    private static final String OUTPUT_PATH = "/Users/martinsail/Desktop/AmarIRSV4.0";

    static {
        DefaultSVNOptions options = new DefaultSVNOptions();
        manager = SVNClientManager.newInstance(options, "yffu2", "199746");
    }

    public static void main(String[] args) {

        new SVNLOG().getSVNLog(FRONTEND, 13178, 13180);//前端
        new SVNLOG().getSVNLog(BACKEND, 13210, 13221);//后端
    }


    /**
     * 获取SVN提交日志
     *
     * @param projectPath   代码所在目录
     * @param startRevision 开始版本
     * @param endRevision   结束版本
     */
    public void getSVNLog(String projectPath, Integer startRevision, Integer endRevision) {
        try {
            //数据去重集合
            HashSet<String> rowSet = new HashSet<>();
            //表格数据
            ArrayList<FillData> fillData = new ArrayList<>();
            //登录
            SVNLogClient logClient = manager.getLogClient();

            //字符串处理函数
            String outputFileName;
            Function<String, String> subString;
            if (FRONTEND.equals(projectPath)) {
                outputFileName = OUTPUT_PATH + File.separator + "代码改动-前端.xlsx";
                subString = s -> s.substring(s.indexOf("projects") - 1);
            } else {
                outputFileName = OUTPUT_PATH + File.separator + "代码改动-后端.xlsx";
                subString = s -> s.substring(s.indexOf("irs-server") + 10);

            }
            //SVNLog处理
            ISVNLogEntryHandler isvnLogEntryHandler = logEntry -> {
                for (String key : logEntry.getChangedPaths().keySet()) {
                    //判断集合是否有值, 有，则跳过，操作为首次更改动作
                    if (!rowSet.contains(key)) {
                        rowSet.add(key);
                        //填充数据
                        FillData data = new FillData();
                        data.setModule("");//开发模块
                        data.setFunctionPoint("");//功能点
                        data.setCodePath(subString.apply(key));//代码路径
                        data.setOperation(SVNLogEntryType.getNameById(logEntry.getChangedPaths().get(key).getType() + ""));//操作
                        data.setRemark("");//备注
                        fillData.add(data);
                    }
//                    System.out.println("key= " + key + " and value= " + logEntry.getChangedPaths().get(key).getType());
                }
            };

            File[] files = new File[]{new File(projectPath)};

            logClient.doLog(files, SVNRevision.create(startRevision), SVNRevision.create(startRevision), SVNRevision.create(endRevision),
                    true, true, 999, isvnLogEntryHandler);

            //生成Excel
            sheetsWrite(outputFileName, fillData);
            copy(projectPath, outputFileName);

        } catch (SVNException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 填充Excel,生成代码改动记录Excel
     *
     * @param outputFileName 输出文件完整路径
     * @param fillData       填充数据
     */
    private void sheetsWrite(String outputFileName, List<FillData> fillData) {
        fillData.sort(Comparator.comparing(FillData::getCodePath));
        ClassPathResource classPathResource = new ClassPathResource("template.xlsx");
        File templateFilePath;
        try {
            templateFilePath = classPathResource.getFile();
            EasyExcel.write(outputFileName).withTemplate(templateFilePath).sheet().doFill(fillData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 复制文件
     *
     * @param projectPath 工程路径
     * @param excelPath   excel所在完整路径
     */
    private void copy(String projectPath, String excelPath) {
        ExcelUtil.getReader(excelPath).readAll().stream().forEach(map -> {
            FileUtil.copyFile(projectPath + map.get("代码路径"), OUTPUT_PATH + projectPath.substring(projectPath.lastIndexOf(File.separator)) + map.get("代码路径"), REPLACE_EXISTING);
        });
    }

    /**
     * 操作类型枚举
     */
    enum SVNLogEntryType {
        TYPE_ADDED("A", "新增"),
        TYPE_DELETED("D", "删除"),
        TYPE_MODIFIED("M", "修改"),
        TYPE_REPLACED("R", "替换");

        private String id;
        private String name;

        SVNLogEntryType(String id, String name) {
            this.id = id;
            this.name = name;
        }

        /**
         * 根据id返回名称
         *
         * @param id
         * @return
         */
        public static String getNameById(String id) {
            for (SVNLogEntryType item : SVNLogEntryType.values()) {
                if (item.id.equalsIgnoreCase(id)) {
                    return item.name;
                }
            }
            return "";
        }

    }
}
