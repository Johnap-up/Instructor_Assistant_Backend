package org.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.CheckRoomRecord;
import org.example.entity.dto.Student;
import org.example.entity.dto.charts.CRRDoUndo;
import org.example.entity.vo.request.CRRSubmitVO;
import org.example.entity.vo.request.saveDataVO.CRRSaveVO;
import org.example.entity.vo.response.CheckRoomRecordShowVO;
import org.example.mapper.CheckRoomRecordMapper;
import org.example.mapper.StudentMapper;
import org.example.service.CheckRoomRecordService;
import org.example.utils.ConstUtil;
import org.example.utils.ServiceImplUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckRoomRecordServiceImpl extends ServiceImpl<CheckRoomRecordMapper, CheckRoomRecord> implements CheckRoomRecordService {
    @Resource
    StudentMapper studentMapper;
    @Resource
    CheckRoomRecordMapper checkRoomRecordMapper;
    @Override
    public CheckRoomRecordShowVO getRecord(String taskId, int id) {
        Student student = studentMapper.selectStudentById(id);
        String dormitory = student.getDormitory();
        String room = student.getRoom();
        List<String> roomMates = studentMapper.selectStudentByDormitoryAndRoom(dormitory, room);
        CheckRoomRecord dto = baseMapper.selectOne(Wrappers.<CheckRoomRecord>query()
                .eq("isDone", true)
                .eq("taskId", taskId)
                .eq("dormitory", dormitory)
                .eq("room", room));
        if (dto == null)
            return null;
        CheckRoomRecordShowVO vo = new CheckRoomRecordShowVO();
        //复制了id，content，title，submitTime， 还差user，images，text
        BeanUtils.copyProperties(dto, vo);
        vo.setUser(new CheckRoomRecordShowVO.User(dormitory, room, roomMates));
        if (dto.getContent() == null)
            return vo;
        JSONArray ops = JSONObject.parseObject(dto.getContent()).getJSONArray("ops");
        return ServiceImplUtil.setTextAndImages(vo, ops);
    }

    @Override
    public synchronized String submitRecord(int id, CRRSubmitVO vo) {
        Student student = studentMapper.selectStudentById(id);
        String dormitory = student.getDormitory();
        String room = student.getRoom();
        CheckRoomRecord temp = baseMapper.selectOne(Wrappers.<CheckRoomRecord>query()
                .eq("isDone", true)
                .eq("taskId", vo.getTaskId())
                .eq("dormitory", dormitory)
                .eq("room", room));
        if (temp != null)
            return "已提交过该任务的记录";
        boolean flag = checkRoomRecordMapper.updateCRR(vo.getTaskId(), dormitory, room, new Date(), vo.getContent(), vo.getTitle(), true);
//        CheckRoomRecord dto = new CheckRoomRecord(null, dormitory, room, vo.getTaskId(), new Date(), vo.getContent(), vo.getTitle(), true);
        return flag ? null : "提交失败, 请联系管理员";
    }

    @Override
    public String updateRecord(int id, CRRSaveVO vo) {
        if (ServiceImplUtil.textLimitCheck(JSONObject.parseObject(vo.getContent()), 200))
            return "内容过长, 更新失败";
        Student student = studentMapper.selectStudentById(id);
        String dormitory = student.getDormitory();
        String room = student.getRoom();
        boolean flag = checkRoomRecordMapper.update(null, Wrappers.<CheckRoomRecord>update()
                .eq("taskId", vo.getTaskId())
                .eq("dormitory", dormitory)
                .eq("room", room)
                .set("content", vo.getContent())
                .set("title", vo.getTitle())) > 0;
        return flag ? null : "更新失败";
    }

    @Override
    public List<CheckRoomRecordShowVO> getAllRecords(String taskId, int pageNum) {
        Page<CheckRoomRecord> page = Page.of(pageNum, ConstUtil.TASK_PAGE_SIZE);
        checkRoomRecordMapper.selectPage(page, Wrappers.<CheckRoomRecord>query().eq("taskId", taskId).eq("isDone", true).orderByAsc("submitTime"));
        return page.getRecords().stream().map(this::convertToCRRShowVO).toList();
    }

    @Override
    public Map<String, List<CRRDoUndo>> getDoUndo(String taskId) {
        Map<String, List<CRRDoUndo>> map = new HashMap<>();
        map.put("do", checkRoomRecordMapper.getDoUndo(taskId, true));
        map.put("undo", checkRoomRecordMapper.getDoUndo(taskId, false));
        return map;
    }

    private CheckRoomRecordShowVO convertToCRRShowVO(CheckRoomRecord dto){
        CheckRoomRecordShowVO vo = new CheckRoomRecordShowVO();
        //copy了id, content, title, submitTime
        BeanUtils.copyProperties(dto, vo);
        String dormitory = dto.getDormitory();
        String room = dto.getRoom();
        List<String> roomMates = studentMapper.selectStudentByDormitoryAndRoom(dormitory, room);
        vo.setUser( new CheckRoomRecordShowVO.User(dormitory, room, roomMates));

        if (dto.getContent() == null)
            return vo;
        JSONArray ops = JSONObject.parseObject(dto.getContent()).getJSONArray("ops");
        return ServiceImplUtil.setTextAndImages(vo, ops);
    }

}
