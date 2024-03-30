package org.example.utils;

import org.springframework.stereotype.Component;

@Component
public class ConstUtil {
    //退出登录用的黑名单
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";

    //过滤器的常数
    public static final int ORDER_CORS = -102;
    public static final int ORDER_LIMIT = -101;

    //限流的常数
    public static final int FLOW_LIMIT_LISTEN_TIME = 3;     //s
    public static final int FLOW_LIMIT_CLOSE_TIME = 30;     //s
    public static final int FLOW_LIMIT_THRESHOLD_COUNT = 10;    //次
    public static final String FLOW_LIMIT_COUNT = "flow:limit:count:";
    public static final String FLOW_LIMIT_BLOCK = "flow:limit:BLOCK:";

    //验证码相关标记常数
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";
    public static final String VERIFY_EMAIL_RESET_CODE = "verify:email:reset:code:";

    //验证码限制时间
    public static final int VERIFY_BLOCK_TIME = 60;

    //前端保存Token的字段
    public static final String ATTR_USER_ID = "id";

    //用到的Minio bucket
    public static final String BUCKET_INSTRUCTOR = "instructor";
}
