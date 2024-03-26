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
    public static String getFlowLimitBlock(String ip){ return ConstUtil.FLOW_LIMIT_BLOCK + ip;}
    public static String getFlowLimitCount(String ip){ return ConstUtil.FLOW_LIMIT_COUNT + ip;}
}
