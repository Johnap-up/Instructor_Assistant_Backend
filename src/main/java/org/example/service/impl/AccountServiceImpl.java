package org.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.Account;
import org.example.entity.vo.request.*;
import org.example.mapper.AccountMapper;
import org.example.service.AccountService;
import org.example.utils.ConstUtil;
import org.example.utils.FlowUtil;
import org.example.utils.StringForRedisUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Resource
    PasswordEncoder encoder;
    @Resource
    AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   //
        Account account = this.findByUsernameOrEmail(username);     //这是登录时用到的函数，会自动调用
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
    public String registerEmailVerifyCode(String type, String email, String ip) {       //虽然名字叫register但就是用来发送验证码的，适合所有类型
        synchronized (ip.intern()){         //加锁，防止多线程
            if (!verifyLimit(ip)){ return "请求频繁，请稍后再试"; }
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            String codeStr = String.valueOf(code);
            Map<String, Object> data = Map.of("type", type, "email", email, "code", codeStr);
            amqpTemplate.convertAndSend("mail", data);          //再去看MailQueueListener部分
            String key;
            switch (type){
                case "register":
                    key=StringForRedisUtil.getVerifyEmailData(email);      //ConstUtil.VERIFY_EMAIL_DATA + email
                    break;
                case "reset":
                    key=StringForRedisUtil.getVerifyEmailResetCode(email); //ConstUtil.VERIFY_EMAIL_RESET_CODE + email
                    break;
                case "modify":
                    key=StringForRedisUtil.getVerifyEmailModifyCode(email);
                    break;
                default:
                    return "未知的请求类型";
            }
            stringRedisTemplate.opsForValue()
                    .set( key , String.valueOf(code), ConstUtil.VERIFY_EMAIL_EFFECTIVE_TIME, TimeUnit.MINUTES);
            return null;
        }
    }
    private boolean verifyLimit(String ip){
        String key = StringForRedisUtil.getVerifyEmailLimit(ip);      //ConstUtil.VERIFY_EMAIL_LIMIT + ip
        return flowUtil.limitOnceCheck(key, ConstUtil.VERIFY_BLOCK_TIME);
    }
    @Override
    public String registerEmailAccount(EmailRegisterVO emailRegisterVO) {
        String email = emailRegisterVO.getEmail();
        String key = StringForRedisUtil.getVerifyEmailData(email);    //ConstUtil.VERIFY_EMAIL_DATA + email
        String code = stringRedisTemplate.opsForValue().get(key);
        if (code == null) return "请先获取验证码";
        if (!code.equals(emailRegisterVO.getCode())) {
            return "验证码错误";
        } else {
            int i = existAccountByEmailOrUsername(email, emailRegisterVO.getUsername());
            switch (i){
                case 1: return  "该邮箱已被注册";
                case 2: return  "该用户名已被注册";
                case 3: return  "该邮箱已被注册，该用户名已被注册";
            }
            String password = encoder.encode(emailRegisterVO.getPassword());
            Account account = new Account(null, emailRegisterVO.getUsername(), password, email, "user", new Date(), null);
            if (this.save(account)){
                stringRedisTemplate.delete(key);        //验证完毕后删除验证码
                return null;
            }
            else
                return "内部错误，请联系管理员";
        }
    }
    private int existAccountByEmailOrUsername(String email, String username){
        int i = 0;
        if (this.baseMapper.exists(Wrappers.<Account>query().eq("email", email))) i += 1;
        if (this.baseMapper.exists(Wrappers.<Account>query().eq("username", username))) i += 2;
        return i;
    }
    @Override
    public String resetEmailConfirm(EmailResetConfirmVO emailResetConfirmVO) {
        Account account = this.findByUsernameOrEmail(emailResetConfirmVO.getEmail());
        if (account == null) return "该邮箱未注册";
        String email = emailResetConfirmVO.getEmail();
        String key = StringForRedisUtil.getVerifyEmailResetCode(email);
        String code = stringRedisTemplate.opsForValue()
                .get(key);
        if (code == null) return "请先获取验证码";
        String requestCode = emailResetConfirmVO.getCode();
        if (requestCode == null) return "验证码为空";
        if (!code.equals(requestCode)) return "验证码错误";
        stringRedisTemplate.delete(key);            //若成功则删除验证码
        return null;
    }
    @Override
    public String resetPassword(EmailResetPasswordVO emailResetPasswordVO) {
        String password = encoder.encode(emailResetPasswordVO.getPassword());
        String email = emailResetPasswordVO.getEmail();
        boolean update = this.update().eq("email", email).set("password", password).update();
        return update ? null : "内部错误，请联系管理员";
    }
    @Override
    public Account findAccountById(int id){ return this.query().eq("id", id).one();}
    @Override
    public String modifyEmail(int id, ModifyEmailVO modifyEmailVO) {
        String email = modifyEmailVO.getEmail();
        String key = StringForRedisUtil.getVerifyEmailModifyCode(email);
        String code = stringRedisTemplate.opsForValue().get(key);
        if (this.baseMapper.exists(Wrappers.<Account>query().eq("email", email)))
            return "该邮箱已被绑定！";
        if (code == null) return "请先获取验证码";
        if (!code.equals(modifyEmailVO.getCode())) return "验证码错误";
        stringRedisTemplate.delete(key);        //验证成功后删除验证码
        this.update().eq("id", id).set("email", email).update();
        return null;
    }
    @Override
    public String changePassword(int id, ChangePasswordVO changePasswordVO) {
        String password = this.query().eq("id",id).one().getPassword();
        if (!encoder.matches(changePasswordVO.getPassword(), password))
            return "原密码错误，请重新输入";
        boolean result = this.update().eq("id",id).set("password", encoder.encode(changePasswordVO.getNew_password())).update();
        return result ? null : "内部错误，请联系管理员";
    }
    @Override
    public String findTidById(int id) {
        return accountMapper.getTidById(id);
    }
}
