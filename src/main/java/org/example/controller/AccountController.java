package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.entity.RestBean;
import org.example.entity.dto.Account;
import org.example.entity.dto.AccountDetails;
import org.example.entity.vo.request.ChangePasswordVO;
import org.example.entity.vo.request.ModifyEmailVO;
import org.example.entity.vo.request.saveDataVO.DetailSaveVO;
import org.example.entity.vo.response.AccountDetailsVO;
import org.example.entity.vo.response.AccountVO;
import org.example.mapper.StudentMapper;
import org.example.service.AccountDetailsService;
import org.example.service.AccountService;
import org.example.utils.ConstUtil;
import org.example.utils.ControllerUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {
    @Resource
    AccountService accountService;
    @Resource
    AccountDetailsService accountDetailsService;
    @Resource
    StudentMapper studentMapper;
    @Resource
    ControllerUtil controllerUtil;
    @GetMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                    @RequestAttribute(ConstUtil.ATTR_ROLE) String role){
        Account account = accountService.findAccountById(id);
        String name;
        if ("instructor".equals(role))
            name = accountDetailsService.findAccountDetailsById(id).getName();
        else if ("student".equals(role))
            name = studentMapper.selectNameBySid(account.getUsername());
        else {
            name = account.getUsername();
        }
        return RestBean.success(account.asViewObject(AccountVO.class, accountVO -> accountVO.setName(name)));
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
        return controllerUtil.messageHandle(() -> accountService.modifyEmail(id, modifyEmailVO));
    }
    @PostMapping("/change-password")
    public RestBean<String> changePassword(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                           @RequestBody @Valid ChangePasswordVO changePasswordVO){
        return controllerUtil.messageHandle(() -> accountService.changePassword(id, changePasswordVO));
    }
}
