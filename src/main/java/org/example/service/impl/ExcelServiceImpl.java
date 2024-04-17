package org.example.service.impl;

import jakarta.annotation.Resource;
import org.example.entity.dto.excel.CRRDoUndoExcel;
import org.example.entity.dto.excel.STRDoUndoExcel;
import org.example.mapper.excel.ExcelMapper;
import org.example.service.ExcelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Resource
    ExcelMapper excelMapper;
    @Override
    public List<CRRDoUndoExcel> getCRRDoUndo(String taskId) {
        return excelMapper.getDoUndo(taskId);
    }

    @Override
    public List<STRDoUndoExcel> getSTRDoUndo(String taskId) {
        return excelMapper.getSTRDoUndo(taskId);
    }
}




















