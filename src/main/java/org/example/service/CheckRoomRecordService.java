package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.CheckRoomRecord;
import org.example.entity.vo.request.CRRSubmitVO;
import org.example.entity.vo.request.saveDataVO.CRRSaveVO;
import org.example.entity.vo.response.CheckRoomRecordShowVO;

import java.util.List;

public interface CheckRoomRecordService extends IService<CheckRoomRecord> {
    CheckRoomRecordShowVO getRecord(String taskId, int id);
    String submitRecord(int id, CRRSubmitVO vo);
    String updateRecord(int id, CRRSaveVO vo);

    /* 老师的接口*/
    List<CheckRoomRecordShowVO> getAllRecords(String taskId, int page);
}
