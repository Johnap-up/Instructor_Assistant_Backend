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
import org.example.entity.dto.Account;
import org.example.entity.dto.AccountDetails;
import org.example.entity.dto.Task;
import org.example.entity.dto.TaskType;
import org.example.entity.vo.request.TaskCreateVO;
import org.example.entity.vo.request.TaskUpdateVO;
import org.example.entity.vo.response.TaskDetailVO;
import org.example.entity.vo.response.TaskPreviewVO;
import org.example.mapper.AccountDetailsMapper;
import org.example.mapper.AccountMapper;
import org.example.mapper.TaskMapper;
import org.example.mapper.TaskTypeMapper;
import org.example.service.TaskService;
import org.example.utils.CacheUtil;
import org.example.utils.ConstUtil;
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
        if (!textLimitCheck(taskCreateVO.getContent()))
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
        System.out.println(task);
        if (save(task)) {
            //要修改缓存
            cacheUtil.deleteCache(ConstUtil.TASK_PREVIEW_CACHE + "*");
            return null;
        }else {
            return "内部错误，请联系管理员";
        }
    }
    private boolean textLimitCheck(JSONObject object){
        if (object == null) return false;
        long length = 0;
        for (Object op : object.getJSONArray("ops")){
            length += JSONObject.from(op).getString("insert").length();
            if (length > 20000) return false;
        }
        return true;
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
        String aid = aidType + strYear + semester + strSequence;
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
    public List<TaskPreviewVO> listTaskByTypeAndPage(int type, int page, int year, int semester, int id) {
        String tid = accountMapper.getTidById(id);          //获取tid
        String key = ConstUtil.TASK_PREVIEW_CACHE + tid + ":" + page + ":" + type + ":" + semester + ":" + year;
        List<TaskPreviewVO> list = cacheUtil.takeListFromCache(key, TaskPreviewVO.class);
        if (list != null) return list;
        Page<Task> mybatisPage = Page.of(page, ConstUtil.TASK_PAGE_SIZE);
        List<Task> tasks;
        if (type == 0){
            if (year ==0 && semester !=0)
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("semester", semester).eq("tid", tid).orderByDesc("issue_time"));
//                tasks = taskMapper.taskListBySemester(semester, tid, page * ConstUtil.TASK_PAGE_SIZE);
            else if (year != 0 && semester == 0)
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("year", year).eq("tid", tid).orderByDesc("issue_time"));
//                tasks = taskMapper.taskListByYear(year, tid, page * ConstUtil.TASK_PAGE_SIZE);
            else if (year == 0)
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("tid", tid).orderByDesc("issue_time"));
//                tasks = taskMapper.listAll(tid, page * ConstUtil.TASK_PAGE_SIZE);
            else
                baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("year", year).eq("semester", semester).eq("tid", tid).orderByDesc("issue_time"));
//                tasks = taskMapper.taskList(year, semester, tid, page * ConstUtil.TASK_PAGE_SIZE);      // * 10
        } else{
            baseMapper.selectPage(mybatisPage, Wrappers.<Task>query().eq("year", year).eq("semester", semester).eq("type", type).eq("tid", tid).orderByDesc("issue_time"));
//            tasks = taskMapper.taskListByType(year, semester, tid, type, page * ConstUtil.TASK_PAGE_SIZE);
        }
        tasks = mybatisPage.getRecords();
        return getTaskPreviewVOS(id, key, tasks);
    }
    private TaskPreviewVO taskToTaskPreviewVO(Task task){
        TaskPreviewVO taskPreviewVO = new TaskPreviewVO();
        //复制了aid, type, title, issueTime, endTime, tid，剩下text, images, name
        BeanUtils.copyProperties(task, taskPreviewVO);

        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(task.getContent()).getJSONArray("ops");      //quill文本内容
        for (Object op : ops) {
            Object insert = JSONObject.from(op).get("insert");
            if (insert instanceof String text){
                if (previewText.length() >= ConstUtil.TASK_PREVIEW_CONTENT_LENGTH) continue;      //300
                previewText.append(text);
            }else if (insert instanceof Map<?, ?> map){
                Optional.ofNullable(map.get("image"))
                        .ifPresent(obj -> images.add(obj.toString()));
            }
        }
        taskPreviewVO.setText(previewText.length() > ConstUtil.TASK_PREVIEW_CONTENT_LENGTH
                ? previewText.substring(0, ConstUtil.TASK_PREVIEW_CONTENT_LENGTH)
                : previewText.toString());
        taskPreviewVO.setImages(images);
        return taskPreviewVO;
    }

    @Override
    public List<TaskPreviewVO> listAllTask(int page, int id) {
        String tid = accountMapper.getTidById(id);          //获取tid
        String key = ConstUtil.TASK_PREVIEW_CACHE + page;
        List<TaskPreviewVO> list = cacheUtil.takeListFromCache(key, TaskPreviewVO.class);
        if (list != null) return list;
        List<Task> tasks;
        tasks = taskMapper.listAll(tid, page * ConstUtil.TASK_PAGE_SIZE);
        return getTaskPreviewVOS(id, key, tasks);
    }
    @Nullable
    private List<TaskPreviewVO> getTaskPreviewVOS(int id, String key, List<Task> tasks) {
        List<TaskPreviewVO> list;
        if (tasks.isEmpty()) return null;
        list = tasks.stream().map(this::taskToTaskPreviewVO).toList();
        String name = accountDetailsMapper.selectById(id).getName();
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
        AccountDetails accountDetails = accountDetailsMapper.selectOne(Wrappers.<AccountDetails>query().eq("tid", tid));
        Account account = accountMapper.selectById(accountDetails.getId());
        BeanUtils.copyProperties(account, user);
        BeanUtils.copyProperties(accountDetails, user);
        taskDetailVO.setUser(user);
        return taskDetailVO;
    }

    @Override
    public String updateTask(int id, TaskUpdateVO taskUpdateVO) {
        if (!textLimitCheck(taskUpdateVO.getContent()))
            return "文章内容过长，发送失败";
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
        return null;
    }
}














