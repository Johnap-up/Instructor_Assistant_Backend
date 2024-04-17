package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.AccountDetails;
import org.example.entity.vo.request.saveDataVO.DetailSaveVO;
import org.example.mapper.AccountDetailsMapper;
import org.example.service.AccountDetailsService;
import org.example.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {
    @Resource
    LogService logService;
    @Override
    public AccountDetails findAccountDetailsById(int id) {
        return this.getById(id);
    }
    @Override
    public synchronized boolean saveAccountDetails(int id, DetailSaveVO detailSaveVO) {
        logService.insertLog(id, "修改手机号");
        return this.update().eq("id",id).set("phone", detailSaveVO.getPhone()).update();
    }
}
