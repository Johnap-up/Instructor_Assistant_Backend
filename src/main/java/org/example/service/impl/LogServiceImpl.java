package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.Log;
import org.example.entity.vo.response.LogVO;
import org.example.mapper.LogMapper;
import org.example.service.LogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    @Resource
    LogMapper logMapper;
    @Override
    public boolean insertLog(int uid, String content) {
        return logMapper.insert(new Log(null,content, uid, new Date())) > 0;
    }

    @Override
    public List<LogVO> getLogs(int id) {
        return logMapper.getLogList(id);
    }
}
