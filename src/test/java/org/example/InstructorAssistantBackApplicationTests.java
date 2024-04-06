package org.example;

import jakarta.annotation.Resource;
import org.example.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InstructorAssistantBackApplicationTests {

    @Resource
    StudentMapper studentMapper;

    @Test
    void contextLoads() {
        System.out.println("2021".substring(2));
//        Date date = new Date();
//        System.out.println(date);
//        Calendar cal = Calendar.getInstance();
//        int day = cal.get(Calendar.DATE);
//        int month = cal.get(Calendar.MONTH) + 1;
//        int year = cal.get(Calendar.YEAR);
//        int dow = cal.get(Calendar.DAY_OF_WEEK);
//        int dom = cal.get(Calendar.DAY_OF_MONTH);
//        int doy = cal.get(Calendar.DAY_OF_YEAR);
//
//        System.out.println("Current Date: " + cal.getTime());
//        System.out.println("Day: " + day);
//        System.out.println("Month: " + month);
//        System.out.println("Year: " + year);
//        System.out.println("Day of Week: " + dow);
//        System.out.println("Day of Month: " + dom);
//        System.out.println("Day of Year: " + doy);
    }

}
