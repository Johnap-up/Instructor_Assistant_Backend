package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.entity.RestBean;
import org.example.entity.dto.Account;
import org.example.entity.dto.AccountDetails;
import org.example.entity.vo.request.ChangePasswordVO;
import org.example.entity.vo.request.DetailSaveVO;
import org.example.entity.vo.request.ModifyEmailVO;
import org.example.entity.vo.response.AccountDetailsVO;
import org.example.entity.vo.response.AccountVO;
import org.example.service.AccountDetailsService;
import org.example.service.AccountService;
import org.example.utils.ConstUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

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
    @PostMapping("/modify-email")
    public RestBean<String> modifyEmail(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                        @RequestBody @Valid ModifyEmailVO modifyEmailVO){
        return this.messageHandle(() -> accountService.modifyEmail(id, modifyEmailVO));
    }
    @PostMapping("/change-password")
    public RestBean<String> changePassword(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                           @RequestBody @Valid ChangePasswordVO changePasswordVO){
        return this.messageHandle(() -> accountService.changePassword(id, changePasswordVO));
    }
    private <T> RestBean<T> messageHandle(Supplier<String> action){
        String msg = action.get();
        return msg == null ? RestBean.success() : RestBean.failure(400, msg);
    }
}
