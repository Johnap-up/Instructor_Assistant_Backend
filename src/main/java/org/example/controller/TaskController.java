package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.RestBean;
import org.example.entity.vo.request.TaskCreateVO;
import org.example.entity.vo.request.TaskUpdateVO;
import org.example.entity.vo.response.TaskDetailVO;
import org.example.entity.vo.response.TaskPreviewVO;
import org.example.entity.vo.response.TaskTypeVO;
import org.example.service.TaskService;
import org.example.utils.ConstUtil;
import org.example.utils.ControllerUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Resource
    TaskService taskService;
    @Resource
    ControllerUtil controllerUtil;
    @GetMapping("/types")
    public RestBean<List<TaskTypeVO>> listTypes(){
        return RestBean.success(taskService.listTypes()
                .stream()
                .map(type -> type.asViewObject(TaskTypeVO.class))
                .toList());
    }
    @PostMapping("/create-task")
    public RestBean<Void> createTask(@Valid @RequestBody TaskCreateVO taskCreateVO,
                                     @RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        return controllerUtil.messageHandle(() -> taskService.createTask(id, taskCreateVO));
    }
    @GetMapping("/list-task")
    public RestBean<List<TaskPreviewVO>> listTask(@RequestParam @Min(0) int page,
                                                  @RequestParam @Min(0) int type,
                                                  @RequestParam int year,
                                                  @RequestParam int semester,
                                                  @RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        return RestBean.success(taskService.listTaskByTypeAndPage(type, page + 1, year, semester, id)); //Pagination start from 1
    }
    @GetMapping("/list-all-task")
    public RestBean<List<TaskPreviewVO>> listAllTask(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                                     @RequestParam @Min(0) int page){
        return RestBean.success(taskService.listAllTask(page, id));
    }
    @GetMapping("/task-detail")
    public RestBean<TaskDetailVO> task(@RequestParam @Min(0) String taskId){
        return RestBean.success(taskService.getTaskDetail(taskId));
    }
    @PostMapping("/update-task")
    public RestBean<Void> updateTask(@Valid @RequestBody TaskUpdateVO taskUpdateVO,
                                     @RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        return controllerUtil.messageHandle(() -> taskService.updateTask(id, taskUpdateVO));
    }
//    @PostMapping("/add-comment")
//    public RestBean<Void> addComment(@RequestParam String taskId,
//                                     @RequestParam String content,
//                                     @RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
//        return controllerUtil.messageHandle(() -> taskService.addComment(taskId, content, id));
//    }
}
