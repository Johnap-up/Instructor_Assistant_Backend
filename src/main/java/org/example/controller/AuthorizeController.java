package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.example.entity.RestBean;
import org.example.entity.vo.request.EmailRegisterVO;
import org.example.entity.vo.request.EmailResetConfirmVO;
import org.example.entity.vo.request.EmailResetPasswordVO;
import org.example.service.AccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AccountService accountService;
    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                          @RequestParam @Pattern(regexp = "(reset|register|modify)") String type,
                                          HttpServletRequest request){
        return this.messageHandle(()->accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr()));    //与下面的等价
//        String message = accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr());
//        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Validated EmailRegisterVO emailRegisterVO){
        return messageHandle(emailRegisterVO, accountService::registerEmailAccount);     //() -> accountService.registerEmailAccount(emailRegisterVO)
    }
    @PostMapping("/reset-password")
    public RestBean<Void> resetPassword(@RequestBody @Validated EmailResetPasswordVO resetPasswordVO){
        return messageHandle(resetPasswordVO, accountService::resetPassword);     //() -> accountService.resetPassword(resetPasswordVO)
    }
    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Validated EmailResetConfirmVO resetConfirmVO){
        return messageHandle(resetConfirmVO, accountService::resetEmailConfirm); //() -> accountService.resetEmailConfirm(resetConfirmVO)
    }
    private <T> RestBean<Void> messageHandle(T vo, Function<T, String> function){
        return this.messageHandle(() -> function.apply(vo));
    }
    private RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
}






















