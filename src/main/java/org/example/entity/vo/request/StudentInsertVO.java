package org.example.entity.vo.request;

import lombok.Data;

@Data
public class StudentInsertVO {
    private String sid;
    private String name;
    private int gender;
    private String dormitory;
    private String room;
    private int classroom;
    private String qq;
    private String phone;
}
