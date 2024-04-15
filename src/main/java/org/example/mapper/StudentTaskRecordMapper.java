package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.entity.dto.StudentTaskRecord;

import java.util.Date;

@Mapper
public interface StudentTaskRecordMapper extends BaseMapper<StudentTaskRecord> {
    @Update("update student_task_record set isDone = #{isDone}, score = #{score}, " +
            "submitTime = #{submitTime}, content = #{content}, title = #{title} where taskId = #{taskId} and sid = #{sid}")
    Boolean updateSTRByTaskId(@Param("taskId") String taskId, @Param("sid") String sid, @Param("isDone") boolean isDone,
                              @Param("score") Integer score, @Param("submitTime")Date submitTime, @Param("content") String content,
                              @Param("title") String title);
}
