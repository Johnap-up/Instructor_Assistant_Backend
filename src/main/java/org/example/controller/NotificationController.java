package org.example.controller;

import jakarta.annotation.Resource;
import org.example.entity.RestBean;
import org.example.entity.vo.response.NotificationVO;
import org.example.service.NotificationService;
import org.example.utils.ConstUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Resource
    NotificationService service;

    @GetMapping("/list")
    public RestBean<List<NotificationVO>> list(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        return RestBean.success(service.findUserNotification(id));
    }
}
