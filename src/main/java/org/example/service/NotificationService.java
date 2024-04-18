package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Notification;
import org.example.entity.vo.response.NotificationVO;

import java.util.List;

public interface NotificationService extends IService<Notification> {
    List<NotificationVO> findUserNotification(int id);
    void addNotification(String tid, String title, String content, String type, String url, String taskId);
}
