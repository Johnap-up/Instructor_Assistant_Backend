package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.dto.AccountDetails;
import org.example.entity.vo.request.DetailSaveVO;
import org.example.mapper.AccountDetailsMapper;
import org.example.service.AccountDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {
    @Override
    public AccountDetails findAccountDetailsById(int id) {
        return this.getById(id);
    }
    @Override
    public synchronized boolean saveAccountDetails(int id, DetailSaveVO detailSaveVO) {
        return this.update().eq("id",id).set("phone", detailSaveVO.getPhone()).update();
    }
}
