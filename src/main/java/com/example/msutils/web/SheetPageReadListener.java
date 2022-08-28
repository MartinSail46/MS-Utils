package com.example.msutils.web;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * 自定义监听器
 * 在PageReadListener基础上添加自定义读取表头
 *
 * @author yffu
 */
@Slf4j
public class SheetPageReadListener<T> implements ReadListener<T> {
    /**
     * 单次处理的数据数量
     */
    public static int BATCH_COUNT = 100;

    /**
     * 数据暂存
     */
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * consumer
     */
    private final Consumer<List<T>> consumer;

    public SheetPageReadListener(Consumer<List<T>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            consumer.accept(cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            consumer.accept(cachedDataList);
        }
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> map = ConverterUtils.convertToStringMap(headMap, context);
        EasyExcelReader.HEADERLIST.add(map);//将每行读取的表头数据，存入全局变量中
    }
}
