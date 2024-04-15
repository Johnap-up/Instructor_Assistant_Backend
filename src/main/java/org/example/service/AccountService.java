package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Account;
import org.example.entity.vo.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findByUsernameOrEmail(String username);
    Account findAccountById(int id);
    String findTidById(int id);
    String registerEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO emailRegisterVO);
    String resetEmailConfirm(EmailResetConfirmVO emailResetConfirmVO);
    String resetPassword(EmailResetPasswordVO emailResetPasswordVO);
    String modifyEmail(int id, ModifyEmailVO modifyEmailVO);
    String changePassword(int id, ChangePasswordVO changePasswordVO);
    Map<String, String> getAllStudentIdEmail();
}
