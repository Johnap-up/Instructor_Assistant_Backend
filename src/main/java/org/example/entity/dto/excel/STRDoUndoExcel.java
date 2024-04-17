package org.example.entity.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.converter.IsDoneConverter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class STRDoUndoExcel {
    @ExcelProperty(value = "学号", index = 0)
    String sid;
    @ExcelProperty(value = "姓名", index = 1)
    String name;
    @ExcelProperty(value = "QQ", index = 2)
    String qq;
    @ExcelProperty(value = "任务Id", index = 3)
    String taskId;
    @ExcelProperty(value = "完成与否", index = 4, converter = IsDoneConverter.class)
    Boolean isDone;
}
