package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.AccountDetails;
import org.example.entity.vo.request.saveDataVO.DetailSaveVO;

public interface AccountDetailsService extends IService<AccountDetails> {
    AccountDetails findAccountDetailsById(int id);
    boolean saveAccountDetails(int id, DetailSaveVO detailSaveVO);
}
