package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.Log;
import org.example.entity.vo.response.LogVO;

import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
    @Select("select content, time, id from log where uid = #{uid} order by time desc ")
    List<LogVO> getLogList(@Param("uid") int uid);
}
