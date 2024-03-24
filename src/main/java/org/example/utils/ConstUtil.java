package org.example.utils;

import org.springframework.stereotype.Component;

@Component
public class ConstUtil {
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";
    public static final int ORDER_CORS = -102;
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";
    public static final int VERIFY_BLOCK_TIME = 60;
}
