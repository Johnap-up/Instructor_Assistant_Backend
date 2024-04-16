package org.example.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConstUtil {
    //退出登录用的黑名单
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";

    //过滤器的常数
    public static final int ORDER_CORS = -102;
    public static final int ORDER_LIMIT = -101;

    //限流的常数
    public static final int FLOW_LIMIT_LISTEN_TIME = 3;     //s
    public static final int FLOW_LIMIT_CLOSE_TIME = 30;     //s
    public static final int FLOW_LIMIT_THRESHOLD_COUNT = 50;    //次
    public static final String FLOW_LIMIT_COUNT = "flow:limit:count:";
    public static final String FLOW_LIMIT_BLOCK = "flow:limit:BLOCK:";

    //验证码相关标记常数
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";                  //限制发送邮件频率，每VERIFY_BLOCK_TIME才可以发送
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";                    //注册验证邮箱用，表示这一类验证码
    public static final String VERIFY_EMAIL_RESET_CODE = "verify:email:reset:code:";        //重置密码时用的
    public static final String VERIFY_EMAIL_MODIFY_CODE = "verify:email:modify:code:";      //修改邮箱用的验证码

    //验证码限制时间
    public static final int VERIFY_BLOCK_TIME = 60;             //60s后重新发送
    public static final int VERIFY_EMAIL_EFFECTIVE_TIME = 3;    //有效时长3min

    //前端保存Token的字段
    public static final String ATTR_USER_ID = "id";
    public static final String ATTR_ROLE = "role";

    //用到的Minio bucket
    public static final String BUCKET_INSTRUCTOR = "instructor";

    //分页常数
    public static final int TASK_PAGE_SIZE = 10;

    //任务相关常数
    public static final int TASK_PREVIEW_CONTENT_LENGTH = 300;
    public static final String TASK_PREVIEW_CACHE = "task:preview:cache:";
    public static final int TASK_PREVIEW_CACHE_EXPIRE = 60;         //单位s
}
