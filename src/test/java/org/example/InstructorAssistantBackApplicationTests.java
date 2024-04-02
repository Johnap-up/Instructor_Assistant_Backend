package org.example;

import jakarta.annotation.Resource;
import org.example.entity.vo.response.StudentActivityRate;
import org.example.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class InstructorAssistantBackApplicationTests {

    @Resource
    StudentMapper studentMapper;

    @Test
    void contextLoads() {
        List<StudentActivityRate> list = studentMapper.getFinishRate(2021, "212101", 1);
        Map<String, String> map = list.stream().collect(Collectors.toMap(
                StudentActivityRate::getSid,
                studentActivityRate -> studentActivityRate.getDoneNum() + "/" + studentActivityRate.getTotalNum()
        ));
        System.out.println(map);
    }

}
