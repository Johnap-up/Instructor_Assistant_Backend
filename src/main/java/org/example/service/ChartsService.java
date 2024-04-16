package org.example.service;

import java.util.List;
import java.util.Map;

public interface ChartsService {
    Map<String, Integer> getDormitory();
    Map<String, List<Integer>> getGender();
    Map<String, Float> getLearningClassroom();
    Map<String, List<Float>> getLatestClassroom();
}
