package org.example;

import jakarta.annotation.Resource;
import org.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InstructorAssistantBackApplicationTests {

    @Resource
    StudentService studentService;

    @Test
    void contextLoads() {
        studentService.getSelectStudents(null, null);
    }
}
