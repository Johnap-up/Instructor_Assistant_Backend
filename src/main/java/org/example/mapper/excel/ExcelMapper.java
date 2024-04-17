package org.example.mapper.excel;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.excel.CRRDoUndoExcel;
import org.example.entity.dto.excel.STRDoUndoExcel;

import java.util.List;

@Mapper
public interface ExcelMapper {
    @Select("select dormitory, room, isDone from check_room_record where taskId = #{taskId} order by isDone desc, dormitory, room")
    List<CRRDoUndoExcel> getDoUndo(@Param("taskId") String taskId);

    @Select("select s.sid, s.name, s.qq, str.taskId, str.isDone from student_task_record str left join student s on str.sid = s.sid where str.taskId = #{taskId}")
    List<STRDoUndoExcel> getSTRDoUndo(@Param("taskId") String taskId);
}


