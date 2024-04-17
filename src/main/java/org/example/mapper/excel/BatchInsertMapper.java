package org.example.mapper.excel;

import java.util.List;

public interface BatchInsertMapper<T>{
    void batchInsert(List<T> list);
}
