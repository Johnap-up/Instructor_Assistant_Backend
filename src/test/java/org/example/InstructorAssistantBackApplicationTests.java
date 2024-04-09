package org.example;

import jakarta.annotation.Resource;
import org.example.mapper.AccountMapper;
import org.example.mapper.StudentMapper;
import org.example.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InstructorAssistantBackApplicationTests {

    @Resource
    StudentMapper studentMapper;
    @Resource
    TaskMapper taskMapper;
    @Resource
    AccountMapper accountMapper;

    @Test
    void contextLoads() {
//        System.out.println(accountMapper.selectById(3));
//        System.out.println(taskMapper.selectById("0123201"));
//        System.out.println(taskMapper.listAll());
        System.out.println(taskMapper.taskList(2023, 2, "212101", 0));
//        System.out.println("2021".substring(2));
    }
}
