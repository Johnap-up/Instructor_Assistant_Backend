package org.example.controller.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.RestBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationController {
    @ExceptionHandler(ConstraintViolationException.class)
    public RestBean<Void> validateError(ValidationException exception) {
        log.warn("Resolved [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(400, "请求参数有误xxxxxxxxxxx");
    }
}