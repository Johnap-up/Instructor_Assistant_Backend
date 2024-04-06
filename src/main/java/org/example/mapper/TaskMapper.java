package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.Task;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    @Select("select MAX(sequence) from task where type = #{type} and year = #{year} and semester = #{semester} and tid = #{tid}")
    Integer getLatestSequence(@Param("type") int type, @Param("year") int year, @Param("semester") int semester, @Param("tid") String tid);
}
