package org.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entity.dto.*;
import org.example.entity.dto.charts.DoUndo;
import org.example.entity.vo.request.SubmitRecordVO;
import org.example.entity.vo.request.TaskCreateVO;
import org.example.entity.vo.request.TaskUpdateVO;
import org.example.entity.vo.request.saveDataVO.StudentRecordSavaVO;
import org.example.entity.vo.response.SubmitRecordShowVO;
import org.example.entity.vo.response.TaskDetailVO;
import org.example.entity.vo.response.TaskPreviewVO;
import org.example.mapper.*;
import org.example.service.LogService;
import org.example.service.NotificationService;
import org.example.service.TaskService;
import org.example.utils.CacheUtil;
import org.example.utils.ConstUtil;
import org.example.utils.ServiceImplUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    private TaskTypeMapper taskTypeMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private AccountDetailsMapper accountDetailsMapper;
    @Resource
    private CacheUtil cacheUtil;
    @Resource
    AccountMapper accountMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    StudentTaskRecordMapper studentTaskRecordMapper;
    @Resource
    CheckRoomRecordMapper checkRoomRecordMapper;
    @Resource
    NotificationService notificationService;
    @Resource
    LogService logService;

    private Set<Integer> types = null;
    @PostConstruct
    private void init(){
        types =this.listTypes()
                .stream()
                .map(TaskType::getId)
                .collect(Collectors.toSet());
    }
    @Override
    public List<TaskType> listTypes() {
        return taskTypeMapper.getAllTaskType();
    }

    @Override
    public String createTask(int uid, TaskCreateVO taskCreateVO) {
        if (ServiceImplUtil.textLimitCheck(taskCreateVO.getContent(), 20000))
            return "文章内容过长，发送失败";
        if (!types.contains(taskCreateVO.getType()))
            return "任务类型不存在";
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateVO, task);

        // 获取信息
        String tid = accountDetailsMapper.selectById(uid).getTid();
        int type = taskCreateVO.getType();
        AidYearSemester aidYearSemester = genAid(type, tid);

        //task属性设置, title，type，issueTime，endTime已经通过copy函数设置了
        task.setTaskId(aidYearSemester.getAid());
        task.setTid(tid);
        task.setContent(taskCreateVO.getContent().toJSONString());
        task.setSequence(aidYearSemester.getSequence());
        task.setSemester(aidYearSemester.getSemester());
        task.setYear(aidYearSemester.getYear());
        if (taskMapper.insert(task) > 0) {
            logService.insertLog(uid, "创建任务，标题：" + task.getTitle() + "，taskId：" + task.getTaskId());
            notificationService.addNotification(tid, "您有新的任务待提交", task.getTitle() != null ? task.getTitle() : "空空", "success", "/index/task/task-detail/" + task.getTaskId(), task.getTaskId());
            //要修改缓存
            cacheUtil.deleteCache(ConstUtil.TASK_PREVIEW_CACHE + "*");
            return null;
        }else {
            return "内部错误，请联系管理员";
        }
    }
    private AidYearSemester genAid(int type, String tid){            //需要定义学期是如何定义的，假定9月开学，3月开学
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int semester;
        if (month < 9 && month >= 3){
            year -= 1;              //2021.6就是第二学期
            semester = 2;
        }else {
            if (month < 3)
                year -= 1;
            semester = 1;
        }
        String strYear = String.valueOf(year).substring(2);
        Integer latestSequence = taskMapper.getLatestSequence(type, year, semester, tid);
        latestSequence = latestSequence == null ? 1 : latestSequence + 1;
        String aidType = type > 9 ? "" + type : "0" + type;
        String strSequence = latestSequence > 9 ? "" + latestSequence : "0" + latestSequence;
        String aid = aidType + strYear + semester + strSequence + tid;
        return new AidYearSemester(aid, year, semester, latestSequence);
    }
    @Data
    @AllArgsConstructor
    static
    class AidYearSemester{
        String aid;
        int year;
        int semester;
        int sequence;
    }

    @Override
    public List<TaskPreviewVO> listTaskByTypeAndPage(int type, int page, int year, int semester, int id, String role) {
        String tid;
        if ("instructor".equals(role))
            tid = accountMapper.getTidById(id);
        else
            tid = studentMapper.selectStudentById(id).getTid();
        String key = ConstUtil.TASK_PREVIEW_CACHE + tid + ":" + page + ":" + type + ":" + semester + ":" + year;
        List<TaskPreviewVO> list = cacheUtil.takeListFromCache(key, TaskPreviewVO.class);
        if (list != null) return list;
        Page<Task> mybatisPage = Page.of(page, ConstUtil.TASK_PAGE_SIZE);
        List<Task> tasks;
        if (type == 0){
            if (year ==0 && semester !=0)
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("semester", semester).eq("tid", tid).orderByDesc("issue_time"));
            else if (year != 0 && semester == 0)
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("year", year).eq("tid", tid).orderByDesc("issue_time"));
            else if (year == 0)
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("tid", tid).orderByDesc("issue_time"));
            else
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("year", year).eq("semester", semester).eq("tid", tid).orderByDesc("issue_time"));
        } else{
            baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("year", year).eq("semester", semester).eq("type", type).eq("tid", tid).orderByDesc("issue_time"));
//            tasks = taskMapper.taskListByType(year, semester, tid, type, page * ConstUtil.TASK_PAGE_SIZE);
        }
        tasks = mybatisPage.getRecords();
        return getTaskPreviewVOS(tid, key, tasks);
    }
    private TaskPreviewVO taskToTaskPreviewVO(Task task){
        TaskPreviewVO taskPreviewVO = new TaskPreviewVO();
        //复制了aid, type, title, issueTime, endTime, tid，剩下text, images, name
        BeanUtils.copyProperties(task, taskPreviewVO);

        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(task.getContent()).getJSONArray("ops");      //quill文本内容
        ServiceImplUtil.shortContent(ops, previewText, obj -> images.add(obj.toString()));
        taskPreviewVO.setText(previewText.length() > ConstUtil.TASK_PREVIEW_CONTENT_LENGTH
                ? previewText.substring(0, ConstUtil.TASK_PREVIEW_CONTENT_LENGTH)
                : previewText.toString());
        taskPreviewVO.setImages(images);
        return taskPreviewVO;
    }

    @Override
    public List<TaskPreviewVO> listAllTask(int page, int id, String role) {
        String tid;
        if ("instructor".equals(role))
            tid = accountMapper.getTidById(id);
        else
            tid = studentMapper.selectStudentById(id).getTid();
        String key = ConstUtil.TASK_PREVIEW_CACHE + page;
        List<TaskPreviewVO> list = cacheUtil.takeListFromCache(key, TaskPreviewVO.class);
        if (list != null) return list;
        List<Task> tasks;
        tasks = taskMapper.listAll(tid, page * ConstUtil.TASK_PAGE_SIZE);
        return getTaskPreviewVOS(tid, key, tasks);
    }
    @Nullable
    private List<TaskPreviewVO> getTaskPreviewVOS(String tid, String key, List<Task> tasks) {
        List<TaskPreviewVO> list;
        if (tasks.isEmpty()) return null;
        list = tasks.stream().map(this::taskToTaskPreviewVO).toList();
        String name = accountDetailsMapper.selectByTid(tid).getName();
        for (TaskPreviewVO taskPreviewVO : list)
            taskPreviewVO.setName(name);
        cacheUtil.saveListToCache(key, list, ConstUtil.TASK_PREVIEW_CACHE_EXPIRE);
        return list;
    }

    @Override
    public TaskDetailVO getTaskDetail(String taskId) {
        TaskDetailVO taskDetailVO = new TaskDetailVO();
        Task task = taskMapper.selectById(taskId);
        BeanUtils.copyProperties(task, taskDetailVO);
        TaskDetailVO.User user = new TaskDetailVO.User();
        String tid = task.getTid();
        taskDetailVO.setUser(this.fillUserDetails(user, tid));
        if (task.getType() == 2)
            taskDetailVO.setRecordAmount(checkRoomRecordMapper.selectCount(Wrappers.<CheckRoomRecord>query().eq("taskId", taskId).eq("isDone", true)));
        else
            taskDetailVO.setRecordAmount(studentTaskRecordMapper.selectCount(Wrappers.<StudentTaskRecord>query().eq("taskId", taskId).eq("isDone", true)));
        return taskDetailVO;
    }
    private <T> T fillUserDetails(T target, String tid){
        AccountDetails accountDetails = accountDetailsMapper.selectOne(Wrappers.<AccountDetails>query().eq("tid", tid));
        Account account = accountMapper.selectById(accountDetails.getId());
        BeanUtils.copyProperties(account, target);
        BeanUtils.copyProperties(accountDetails, target);
        return target;
    }

    @Override
    public String updateTask(int id, TaskUpdateVO taskUpdateVO) {
        if (ServiceImplUtil.textLimitCheck(taskUpdateVO.getContent(), 20000))
            return "内容过长，更新失败";
        if (!types.contains(taskUpdateVO.getType()))
            return "任务类型不存在";
        String tid = accountMapper.getTidById(id);
        baseMapper.update(null, Wrappers.<Task>update()
                .eq("tid", tid)
                .eq("taskId", taskUpdateVO.getTaskId())
                .set("title", taskUpdateVO.getTitle())
                .set("content", taskUpdateVO.getContent().toString())
                .set("type", taskUpdateVO.getType())
        );
        logService.insertLog(id, "修改任务，标题：" + taskUpdateVO.getTitle() + "，taskId" + taskUpdateVO.getTaskId());
        return null;
    }

    @Override
    public String deleteTask(int id, String taskId) {
        String tid = accountMapper.getTidById(id);
        Task task = taskMapper.selectById(taskId);
        if (task == null)
            return "任务不存在或已被删除";
        else {
            if (!task.getTid().equals(tid))
                return "您不是该任务的发布者，无法删除";
            baseMapper.deleteById(taskId);
            cacheUtil.deleteCache(ConstUtil.TASK_PREVIEW_CACHE + "*");
            logService.insertLog(id, "删除任务，标题：" + task.getTitle() + "，taskId" + task.getTaskId());
        }
        return null;
    }

    @Override
    public String createSubmitRecord(int id, SubmitRecordVO vo) {
        if(ServiceImplUtil.textLimitCheck(JSONObject.parseObject(vo.getContent()), 200))
            return "内容过长，发送失败";
        Boolean flag = studentTaskRecordMapper.updateSTRByTaskId(vo.getTaskId(),
                studentMapper.selectStudentById(id).getSid(), true, null, new Date(), vo.getContent(), vo.getTitle());
        logService.insertLog(id, "提交作业");
        return flag ? null : "提交失败，请联系管理员";
    }

    @Override
    public List<SubmitRecordShowVO> submitRecordShow(String taskId, int pageNumber) {
        Page<StudentTaskRecord> page = Page.of(pageNumber, ConstUtil.TASK_PAGE_SIZE);
        studentTaskRecordMapper.selectPage(page, Wrappers.<StudentTaskRecord>query().eq("taskId", taskId).eq("isDone", true).orderByAsc("submitTime"));
        return page.getRecords().stream().map(this::convertToSRS).toList();
    }
    private SubmitRecordShowVO convertToSRS(StudentTaskRecord dto){
        SubmitRecordShowVO submitRecordShowVO = new SubmitRecordShowVO();
        //copy了content，id, submitTime, title, 还差text和images和user
        BeanUtils.copyProperties(dto, submitRecordShowVO);
        SubmitRecordShowVO.User user = new SubmitRecordShowVO.User();
        String sid = dto.getSid();
        submitRecordShowVO.setUser(this.fillUserStudentDetail(user, sid));        //之后改为student的，此处为管理员

        if (dto.getContent() == null)
            return submitRecordShowVO;
        JSONArray ops = JSONObject.parseObject(dto.getContent()).getJSONArray("ops");
        return ServiceImplUtil.setTextAndImages(submitRecordShowVO, ops);
    }
    private <T> T fillUserStudentDetail(T target, String sid){
        Account account = accountMapper.getAccountByUsername(sid);
        Student student = studentMapper.selectById(sid);
        BeanUtils.copyProperties(account, target);
        BeanUtils.copyProperties(student, target);
        return target;
    }
    @Override
    public SubmitRecordShowVO getStudentRecord(String taskId, int id) {
        String sid = studentMapper.selectStudentById(id).getSid();
        StudentTaskRecord dto = studentTaskRecordMapper.selectOne(Wrappers.<StudentTaskRecord>query().eq("taskId",taskId).eq("sid", sid).eq("isDone", true));
        return dto == null ? null : this.convertToSRS(dto);
    }

    @Override
    public String updateStudentRecord(int id, StudentRecordSavaVO vo) {
        if (ServiceImplUtil.textLimitCheck(JSONObject.parseObject(vo.getContent()), 200))
            return "内容过长，更新失败";
        String sid = studentMapper.selectStudentById(id).getSid();
        boolean flag = studentTaskRecordMapper.update(null, Wrappers.<StudentTaskRecord>update()
                .eq("taskId", vo.getTaskId())
                .eq("sid", sid)
                .set("content", vo.getContent())
                .set("title", vo.getTitle())
        ) > 0;
        logService.insertLog(id, "修改作业" + vo.getTaskId() +"内容");
        return flag ? null : "更新失败";
    }

    @Override
    public Map<String, List<DoUndo>> doUndo(String taskId) {
        Map<String, List<DoUndo>> map = new HashMap<>();
        map.put("do", studentMapper.selectDoUndo(taskId, true));
        map.put("undo", studentMapper.selectDoUndo(taskId, false));
        return map;
    }
}














