package org.example;

import jakarta.annotation.Resource;
import org.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class InstructorAssistantBackApplicationTests {

    @Resource
    StudentService studentService;
    @Test
    void contextLoads() {
        Map<String, String> map = studentService.getStudentDoneRate(2023, 2, "212101", 3);
        System.out.println(map);
    }
}
