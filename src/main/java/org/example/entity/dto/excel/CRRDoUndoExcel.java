package org.example.entity.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.converter.DormitoryConverter;
import org.example.utils.converter.IsDoneConverter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CRRDoUndoExcel {
    @ExcelProperty(value = "寝室", index = 0, converter = DormitoryConverter.class)
    String dormitory;
    @ExcelProperty(value = "宿舍号", index = 1)
    String room;
    @ExcelProperty(value = "完成与否", index = 2, converter = IsDoneConverter.class)
    Boolean isDone;
}
