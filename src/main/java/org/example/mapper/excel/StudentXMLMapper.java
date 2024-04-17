package org.example.mapper.excel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.dto.excel.StudentExcel;

@Mapper
public interface StudentXMLMapper extends BaseMapper<StudentExcel> , BatchInsertMapper<StudentExcel> {
}
