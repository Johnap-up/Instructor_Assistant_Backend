package org.example.controller;

import jakarta.annotation.Resource;
import org.example.entity.RestBean;
import org.example.service.ChartsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charts")
public class ChartsController {
    @Resource
    ChartsService chartsService;
    @GetMapping("/dormitory")
    public RestBean<Map<String, Integer>> getDormitory() {
        return RestBean.success(chartsService.getDormitory());
    }
    @GetMapping("/gender")
    public RestBean<Map<String, List<Integer>>> getGender() {
        return RestBean.success(chartsService.getGender());
    }
    @GetMapping("/learning-classroom")
    public RestBean<Map<String, Float>> getLearningClassroom() {
        return RestBean.success(chartsService.getLearningClassroom());
    }
    @GetMapping("/latest-classroom")
    public RestBean<Map<String, List<Float>>> getLatestClassroom() {
        return RestBean.success(chartsService.getLatestClassroom());
    }
}
