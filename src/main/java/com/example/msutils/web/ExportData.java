package com.example.msutils.web;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ExportData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表头数据
     */
    private Map<String, Object> headerData;

    /**
     * 列表数据
     */
    private List<?> bodyData;
}
