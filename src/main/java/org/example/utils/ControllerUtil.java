package org.example.utils;

import org.example.entity.RestBean;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ControllerUtil {
    public <T> RestBean<T> messageHandle(Supplier<String> action){
        String msg = action.get();
        return msg == null ? RestBean.success() : RestBean.failure(400, msg);
    }
}
