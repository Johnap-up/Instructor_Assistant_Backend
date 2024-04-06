package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Task;
import org.example.entity.dto.TaskType;
import org.example.entity.vo.request.TaskCreateVO;

import java.util.List;

public interface TaskService extends IService<Task> {
    List<TaskType> listTypes();
    String createTask(int uid, TaskCreateVO taskCreateVO);
}
