package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.entity.dto.Task;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    @Select("select MAX(sequence) from task where type = #{type} and year = #{year} and semester = #{semester} and tid = #{tid}")
    Integer getLatestSequence(@Param("type") int type, @Param("year") int year, @Param("semester") int semester, @Param("tid") String tid);

    @Select("select * from task where semester = #{semester} and tid = #{tid} order by task.type and issue_time desc limit #{start}, 10")
    @Results({
            @Result(column = "issue_time", property = "issueTime"),
            @Result(column = "end_time", property = "endTime")
    })
    List<Task> taskListBySemester(@Param("semester") int semester, @Param("tid") String tid, @Param("start") int start);

    @Select("select * from task where year = #{year} and tid = #{tid} order by task.type and issue_time desc limit #{start}, 10")
    @Results({
            @Result(column = "issue_time", property = "issueTime"),
            @Result(column = "end_time", property = "endTime")
    })
    List<Task> taskListByYear(@Param("year") int year, @Param("tid") String tid, @Param("start") int start);

    @Select("select * from task where task.year = #{year} and task.semester = #{semester} and task.tid = #{tid} order by task.type and issue_time desc limit #{start}, 10")
    @Results({
            @Result(column = "issue_time", property = "issueTime"),
            @Result(column = "end_time", property = "endTime")
    })
    List<Task> taskList(@Param("year") int year, @Param("semester") int semester, @Param("tid") String tid, @Param("start") int start);

    @Select("select * from task where year = #{year} and semester = #{semester} and tid = #{tid} and type = #{type} order by task.type and issue_time desc limit #{start}, 10")
    @Results({
            @Result(column = "issue_time", property = "issueTime"),
            @Result(column = "end_time", property = "endTime")
    })
    List<Task> taskListByType(@Param("year") int year, @Param("semester") int semester, @Param("tid") String tid, @Param("type") int type, @Param("start") int start);

    @Select("select * from task where tid = #{tid} limit #{start}, 10")
    @Results({
            @Result(column = "issue_time", property = "issueTime"),
            @Result(column = "end_time", property = "endTime")
    })
    List<Task> listAll(@Param("tid") String tid, @Param("start") int start);
}
