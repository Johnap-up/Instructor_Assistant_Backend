package org.example.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.RestBean;
import org.example.entity.vo.response.LogVO;
import org.example.service.LogService;
import org.example.utils.ConstUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/log")
public class LogController {
    @Resource
    LogService logService;
    @GetMapping("/get-logs")
    public RestBean<List<LogVO>> getLogs(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        return RestBean.success(logService.getLogs(id));
    }
}
