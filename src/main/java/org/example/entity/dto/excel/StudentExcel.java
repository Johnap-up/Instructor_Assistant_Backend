package org.example.entity.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.example.utils.converter.DormitoryConverter;
import org.example.utils.converter.GenderConverter;

@Data
public class StudentExcel {
    @TableId(type = IdType.INPUT)
    @ExcelProperty(value = "学号", index = 0)
    private String sid;
    @ExcelProperty(value = "姓名", index = 1)
    private String name;
    @ExcelProperty(value = "性别", index = 2, converter = GenderConverter.class)
    private int gender;
    @ExcelProperty(value = "班级", index = 3)
    private int classroom;
    @ExcelProperty(value = "宿舍", index = 4, converter = DormitoryConverter.class)
    private String dormitory;
    @ExcelProperty(value = "寝室号", index = 5)
    private String room;
    @ExcelProperty(value = "QQ", index = 6)
    private String qq;
    @ExcelProperty(value = "电话", index = 7)
    private String phone;
    @ExcelProperty(value = "学院", index = 8)
    private String institute;
    @ExcelProperty(value = "年级", index = 9)
    private int grade;
    @ExcelProperty(value = "导员编号", index = 10)
    private String tid;
}
