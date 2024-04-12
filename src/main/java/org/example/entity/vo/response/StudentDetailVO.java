package org.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetailVO {
    private String sid;
    private String name;
    private int gender;
    private String dormitory;
    private String room;
    private int classroom;
    private String qq;
    private String phone;
    private String institute;
    private int grade;
    private Instructor instructor;

    @Data
    public static class Instructor{
        private String tid;
        private String name;
        private String phone;
    }
}
