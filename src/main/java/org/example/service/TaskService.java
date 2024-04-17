package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Task;
import org.example.entity.dto.TaskType;
import org.example.entity.dto.charts.DoUndo;
import org.example.entity.vo.request.SubmitRecordVO;
import org.example.entity.vo.request.TaskCreateVO;
import org.example.entity.vo.request.TaskUpdateVO;
import org.example.entity.vo.request.saveDataVO.StudentRecordSavaVO;
import org.example.entity.vo.response.SubmitRecordShowVO;
import org.example.entity.vo.response.TaskDetailVO;
import org.example.entity.vo.response.TaskPreviewVO;

import java.util.List;
import java.util.Map;

public interface TaskService extends IService<Task> {
    List<TaskType> listTypes();
    String createTask(int uid, TaskCreateVO taskCreateVO);
    List<TaskPreviewVO> listTaskByTypeAndPage(int type, int page, int year, int semester, int id, String role);
    List<TaskPreviewVO> listAllTask(int page, int id, String role);
    TaskDetailVO getTaskDetail(String taskId);
    String updateTask(int id, TaskUpdateVO taskUpdateVO);
    String deleteTask(int id, String taskId);
    String createSubmitRecord(int id, SubmitRecordVO submitRecordVO);
    List<SubmitRecordShowVO> submitRecordShow(String taskId, int page);
    SubmitRecordShowVO getStudentRecord(String taskId, int id);
    String updateStudentRecord(int id, StudentRecordSavaVO vo);
    Map<String, List<DoUndo>> doUndo(String taskId);
}
