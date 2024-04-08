package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.TaskType;

import java.util.List;

@Mapper
public interface TaskTypeMapper extends BaseMapper<TaskType> {
    @Select("select * from task_type")
    List<TaskType> getAllTaskType();
}
