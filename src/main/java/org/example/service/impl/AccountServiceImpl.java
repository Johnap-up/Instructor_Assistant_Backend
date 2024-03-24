package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.Account;
import org.example.mapper.AccountMapper;
import org.example.service.AccountService;
import org.example.utils.ConstUtil;
import org.example.utils.FlowUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService{
    @Resource
    AmqpTemplate amqpTemplate;          //springCloud中的知识
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    FlowUtil flowUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findByUsernameOrEmail(username);
        if (account == null){
            throw new UsernameNotFoundException("用户不存在或密码错误");
        }
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }
    public Account findByUsernameOrEmail(String usernameOrEmail){
        return this.query()
                .eq("username", usernameOrEmail).or()
                .eq("email", usernameOrEmail)
                .one();
    }
    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()){         //加锁，防止多线程
            if (!verifyLimit(ip)){ return "请求频繁，请稍后再试"; }
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            String codeStr = String.valueOf(code);
            Map<String, Object> data = Map.of("type", type, "email", email, "code", codeStr);
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue()
                    .set(ConstUtil.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }
    private boolean verifyLimit(String ip){
        String key = ConstUtil.VERIFY_EMAIL_LIMIT + ip;
        return flowUtil.limitOnceCheck(key, ConstUtil.VERIFY_BLOCK_TIME);
    }

}
