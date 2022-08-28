package com.example.msutils.web;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ImportData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表头数据,表头每行为一个Map,key值为列数
     */
    private List<Map<Integer, String>> headerData;

    /**
     * 列表数据
     */
    private List<?> bodyData;
}
