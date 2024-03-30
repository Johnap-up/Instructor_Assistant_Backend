package org.example.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.RestBean;
import org.example.utils.ConstUtil;
import org.example.utils.StringForRedisUtil;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Order(ConstUtil.ORDER_LIMIT)
public class FlowLimitFilter extends HttpFilter {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        if (tryCount(ip)){
            chain.doFilter(request, response);
        }else{
            writeBlockMessage(response);
        }
    }
    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden("访问过于频繁，请稍后再试").asJsonString());
    }
    private boolean tryCount(String ip){
        synchronized (ip.intern()){
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(StringForRedisUtil.getFlowLimitBlock(ip))))
                return false;
            return this.limitPeriodCheck(ip);
        }
    }
    private boolean limitPeriodCheck(String ip){
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(StringForRedisUtil.getFlowLimitCount(ip)))){
            long increment = Optional
                    .ofNullable(stringRedisTemplate.opsForValue()
                            .increment(StringForRedisUtil.getFlowLimitCount(ip)))
                    .orElse(0L);
            if (increment > ConstUtil.FLOW_LIMIT_THRESHOLD_COUNT) {
                stringRedisTemplate.opsForValue()
                        .set(StringForRedisUtil.getFlowLimitBlock(ip), "1", ConstUtil.FLOW_LIMIT_CLOSE_TIME, TimeUnit.SECONDS);
                return false;
            }
        } else {
            stringRedisTemplate.opsForValue()
                    .set(StringForRedisUtil.getFlowLimitCount(ip), "1", ConstUtil.FLOW_LIMIT_LISTEN_TIME, TimeUnit.SECONDS);
        }
        return true;
    }
}
