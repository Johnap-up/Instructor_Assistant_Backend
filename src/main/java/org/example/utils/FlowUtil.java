package org.example.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtil {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public boolean limitOnceCheck(String key, int blockTime){                       //60s后重发验证码
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            return false;
        }else {
            System.out.println("ttl");
            stringRedisTemplate.opsForValue()
                    .set(key, "0", blockTime, TimeUnit.SECONDS);
            return true;
        }
    }
}
