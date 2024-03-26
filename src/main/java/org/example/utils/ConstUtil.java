package org.example.utils;

import org.springframework.stereotype.Component;

@Component
public class ConstUtil {
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";

    public static final int ORDER_CORS = -102;
    public static final int ORDER_LIMIT = -101;

    public static final int FLOW_LIMIT_LISTEN_TIME = 3;     //s
    public static final int FLOW_LIMIT_CLOSE_TIME = 30;     //s
    public static final int FLOW_LIMIT_THRESHOLD_COUNT = 10;    //次

    public static final String FLOW_LIMIT_COUNT = "flow:limit:count:";
    public static final String FLOW_LIMIT_BLOCK = "flow:limit:BLOCK:";

    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";
    public static final String VERIFY_EMAIL_RESET_CODE = "verify:email:reset:code:";

    public static final int VERIFY_BLOCK_TIME = 60;
}
