package org.example.utils;

import org.springframework.stereotype.Component;

@Component
public class StringForRedisUtil {
    public static String getVerifyEmailData(String email){
        return ConstUtil.VERIFY_EMAIL_DATA + email;
    }
    public static String getVerifyEmailLimit(String ip){
        return ConstUtil.VERIFY_EMAIL_LIMIT + ip;
    }
    public static String getVerifyEmailResetCode(String email){
        return ConstUtil.VERIFY_EMAIL_RESET_CODE + email;
    }
}
