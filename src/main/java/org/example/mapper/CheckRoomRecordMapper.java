package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.dto.CheckRoomRecord;
import org.example.entity.dto.charts.CRRDoUndo;

import java.util.Date;
import java.util.List;

@Mapper
public interface CheckRoomRecordMapper extends BaseMapper<CheckRoomRecord> {
    @Select("select dormitory, room from check_room_record where taskId = #{taskId} and isDone = #{isDone}")
    List<CRRDoUndo> getDoUndo(@Param("taskId") String taskId, @Param("isDone") boolean isDone);

    @Update("update check_room_record set submitTime = #{submitTime}" +
            ", content = #{content}, title = #{title}, isDone = #{isDone} where taskId = #{taskId} and dormitory = #{dormitory} and room = #{room}")
    Boolean updateCRR(@Param("taskId") String taskId, @Param("dormitory") String dormitory, @Param("room") String room,
                      @Param("submitTime") Date submitTime, @Param("content") String content, @Param("title") String title,
                      @Param("isDone") boolean isDone);
}
