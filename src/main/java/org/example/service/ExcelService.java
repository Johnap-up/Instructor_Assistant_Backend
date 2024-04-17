package org.example.service;

import org.example.entity.dto.excel.CRRDoUndoExcel;
import org.example.entity.dto.excel.STRDoUndoExcel;

import java.util.List;

public interface ExcelService {
    List<CRRDoUndoExcel> getCRRDoUndo(String taskId);
    List<STRDoUndoExcel> getSTRDoUndo(String taskId);
}
