package org.example;

import jakarta.annotation.Resource;
import org.example.service.ChartsService;
import org.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InstructorAssistantBackApplicationTests {

    @Resource
    StudentService studentService;
    @Resource
    ChartsService chartsService;

    @Test
    void contextLoads() {
        System.out.println(chartsService.getLatestClassroom());
    }
}
