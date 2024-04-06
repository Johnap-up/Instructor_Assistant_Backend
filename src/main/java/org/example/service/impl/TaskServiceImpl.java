package org.example.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entity.dto.Task;
import org.example.entity.dto.TaskType;
import org.example.entity.vo.request.TaskCreateVO;
import org.example.mapper.AccountDetailsMapper;
import org.example.mapper.TaskMapper;
import org.example.mapper.TaskTypeMapper;
import org.example.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    private TaskTypeMapper taskTypeMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private AccountDetailsMapper accountDetailsMapper;
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
        return taskTypeMapper.selectList(null);
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
        task.setAid(aidYearSemester.getAid());
        task.setTid(tid);
        task.setContent(taskCreateVO.getContent().toJSONString());
        task.setSequence(aidYearSemester.getSequence());
        task.setSemester(aidYearSemester.getSemester());
        task.setYear(aidYearSemester.getYear());
        System.out.println(task);
        if (save(task)) {
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
}
