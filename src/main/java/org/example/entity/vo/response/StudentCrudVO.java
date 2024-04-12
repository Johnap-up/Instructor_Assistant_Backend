package org.example.entity.vo.response;

import lombok.Data;

@Data
public class StudentCrudVO {
    private String sid;
    private String name;
    private int gender;
    private String dormitory;
    private String room;
    private int classroom;
    private String email;           //在Account表中
    private String qq;
    private String phone;
}
