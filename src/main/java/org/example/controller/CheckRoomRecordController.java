package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.example.entity.RestBean;
import org.example.entity.vo.request.CRRSubmitVO;
import org.example.entity.vo.request.saveDataVO.CRRSaveVO;
import org.example.entity.vo.response.CheckRoomRecordShowVO;
import org.example.service.CheckRoomRecordService;
import org.example.utils.ConstUtil;
import org.example.utils.ControllerUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class CheckRoomRecordController {
    @Resource
    CheckRoomRecordService checkRoomRecordService;
    @Resource
    ControllerUtil controllerUtil;
    @GetMapping("/record")
    public RestBean<CheckRoomRecordShowVO> record(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                                  @RequestParam("taskId") String taskId){
        return RestBean.success(checkRoomRecordService.getRecord(taskId, id));
    }
    @PostMapping("/submit-record")
    public RestBean<Void> submitRecord(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                       @RequestBody @Valid CRRSubmitVO vo){
        return controllerUtil.messageHandle(() -> checkRoomRecordService.submitRecord(id, vo));
    }
    @PostMapping("/update-record")
    public RestBean<Void> updateRecord(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                       @RequestBody @Valid CRRSaveVO vo){
        return controllerUtil.messageHandle(() -> checkRoomRecordService.updateRecord(id, vo));
    }

    /* 老师端的接口*/
    @GetMapping("/records")
    public RestBean<List<CheckRoomRecordShowVO>> records(@RequestParam @Min(0) String taskId,
                                                         @RequestParam @Min(0) int page){
        return RestBean.success(checkRoomRecordService.getAllRecords(taskId, page + 1));
    }
}
