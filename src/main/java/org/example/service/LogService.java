package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Log;
import org.example.entity.vo.response.LogVO;

import java.util.List;

public interface LogService extends IService<Log> {
    boolean insertLog(int uid, String content);
    List<LogVO> getLogs(int id);
}
