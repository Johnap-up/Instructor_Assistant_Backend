package org.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.Notification;
import org.example.entity.vo.response.NotificationVO;
import org.example.mapper.AccountDetailsMapper;
import org.example.mapper.NotificationMapper;
import org.example.mapper.StudentMapper;
import org.example.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Resource
    StudentMapper studentMapper;
    @Resource
    AccountDetailsMapper accountDetailsMapper;
    @Override
    public List<NotificationVO> findUserNotification(int id) {
        if (accountDetailsMapper.selectById(id) != null)
            return null;
        String tid = studentMapper.selectStudentById(id).getTid();
        return this.list(Wrappers.<Notification>query().eq("tid", tid).orderByDesc("time"))
                .stream()
                .map(no -> no.asViewObject(NotificationVO.class)).toList();
    }

    @Override
    public void addNotification(String tid, String title, String content, String type, String url, String taskId) {
        Notification no = new Notification();
        no.setTid(tid);
        no.setTitle(title);
        no.setType(type);
        no.setContent(content);
        no.setUrl(url);
        no.setTime(new Date());
        no.setTaskId(taskId);
        this.save(no);
    }
}











