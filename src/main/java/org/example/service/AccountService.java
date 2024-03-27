package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Account;
import org.example.entity.vo.request.EmailRegisterVO;
import org.example.entity.vo.request.EmailResetConfirmVO;
import org.example.entity.vo.request.EmailResetPasswordVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findByUsernameOrEmail(String username);
    Account findAccountById(int id);
    String registerEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO emailRegisterVO);
    String resetEmailConfirm(EmailResetConfirmVO emailResetConfirmVO);
    String resetPassword(EmailResetPasswordVO emailResetPasswordVO);
}
