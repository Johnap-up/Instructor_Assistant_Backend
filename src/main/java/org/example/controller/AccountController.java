package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.entity.RestBean;
import org.example.entity.dto.Account;
import org.example.entity.dto.AccountDetails;
import org.example.entity.vo.request.DetailSaveVO;
import org.example.entity.vo.response.AccountDetailsVO;
import org.example.entity.vo.response.AccountVO;
import org.example.service.AccountDetailsService;
import org.example.service.AccountService;
import org.example.utils.ConstUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {
    @Resource
    AccountService accountService;
    @Resource
    AccountDetailsService accountDetailsService;
    @GetMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        Account account = accountService.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVO.class));
    }
    @GetMapping("/details")
    public RestBean<AccountDetailsVO> details(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        AccountDetails accountDetails = Optional
                .ofNullable(accountDetailsService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(accountDetails.asViewObject(AccountDetailsVO.class));
    }

    @PostMapping("/save-details")
    public RestBean<String> saveDetails(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                        @RequestBody @Valid DetailSaveVO vo){
        return accountDetailsService.saveAccountDetails(id, vo) ?
                RestBean.success("保存成功") :
                RestBean.failure(400, "保存失败");
    }
}
