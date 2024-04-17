package org.example.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.excel.BatchInsertMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhenjun
 * @date 2022/12/2 15:38
 */
@Slf4j
@Component
// 每次bean都是新的，不要单例
@Scope("prototype")
public abstract class ImportDataListener<T> implements ReadListener<T> {
    protected abstract BatchInsertMapper<T> getMapper();


    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 2;
    private static boolean isFull = false;
    /**
     * 缓存的数据
     */
//    private List<Test> importExcelDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private List<T> importExcelDataList = new ArrayList<>(BATCH_COUNT);


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        importExcelDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (importExcelDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
//            importExcelDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            importExcelDataList = new ArrayList<>(BATCH_COUNT);
            isFull = true;
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库z
        if (isFull&& !importExcelDataList.isEmpty()){
            saveData();
            importExcelDataList = new ArrayList<>(BATCH_COUNT);
            log.info("所有数据解析完成！");
            isFull = false;
        }
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", importExcelDataList.size());
        getMapper().batchInsert(importExcelDataList);
        log.info("存储数据库成功！");
    }
}

