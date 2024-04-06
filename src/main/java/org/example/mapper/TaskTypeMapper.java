package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.dto.TaskType;

@Mapper
public interface TaskTypeMapper extends BaseMapper<TaskType> {
}
