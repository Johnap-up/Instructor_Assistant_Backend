package org.example.service.impl;

import jakarta.annotation.Resource;
import org.example.entity.dto.charts.LearningClassroom;
import org.example.mapper.StudentMapper;
import org.example.mapper.TaskMapper;
import org.example.service.ChartsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartsServiceImpl implements ChartsService {
    @Resource
    StudentMapper studentMapper;
    @Resource
    TaskMapper taskMapper;

    @Override
    public Map<String, Integer> getDormitory() {
        return studentMapper.getDormitory().stream().collect(Collectors.toMap(key -> key.name, val -> val.value));
    }

    @Override
    public Map<String, List<Integer>> getGender() {
        return studentMapper.getGender().stream().collect(Collectors.toMap(key -> key.classroom, val -> List.of(val.man, val.woman)));
    }

    @Override
    public Map<String, Float> getLearningClassroom() {
        List<String> taskList = taskMapper.getLatestTask(5);
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (String s : taskList) {
            stringBuilder.append("'");
            stringBuilder.append(s);
            stringBuilder.append("'");
            stringBuilder.append(",");
        }
        temp = stringBuilder.substring(0, stringBuilder.length() - 1);
        return studentMapper.getLearningClassroom(temp).stream().collect(Collectors.toMap(key -> key.classroom,
                val -> (float)val.done/val.total));
    }

    @Override
    public Map<String, List<Float>> getLatestClassroom() {
        List<String> taskList = taskMapper.getLatestTask(5);
        Map<String, List<Float>> map = new HashMap<>();
        for (String s : taskList) {
            List<LearningClassroom> list = studentMapper.getLatestRate(s);
            for (LearningClassroom learningClassroom : list) {
                if (map.get(learningClassroom.classroom) == null)
                    map.put(learningClassroom.classroom, new ArrayList<>());
                map.get(learningClassroom.classroom).add((float) learningClassroom.done / learningClassroom.total);
            }
        }
        return map;
    }

}
