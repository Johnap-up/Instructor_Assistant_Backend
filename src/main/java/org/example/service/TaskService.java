package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Task;
import org.example.entity.dto.TaskType;
import org.example.entity.vo.request.TaskCreateVO;
import org.example.entity.vo.request.TaskUpdateVO;
import org.example.entity.vo.response.TaskDetailVO;
import org.example.entity.vo.response.TaskPreviewVO;

import java.util.List;

public interface TaskService extends IService<Task> {
    List<TaskType> listTypes();
    String createTask(int uid, TaskCreateVO taskCreateVO);
    List<TaskPreviewVO> listTaskByTypeAndPage(int type, int page, int year, int semester, int id);
    List<TaskPreviewVO> listAllTask(int page, int id);
    TaskDetailVO getTaskDetail(String taskId);
    String updateTask(int id, TaskUpdateVO taskUpdateVO);
}
