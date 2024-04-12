package org.example.entity.vo.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SubmitRecordShowVO {
    int id;
    String content;
    String title;
    String text;
    List<String> images;
    Date submitTime;
    User user;

    @Data
    public static class User {      //这里的User本应是学生的信息，也就是显示student,但因为测试所以改为了管理员的信息
        Integer id;
        String name;
        String role;
        String avatar;
        String phone;
        int gender;
        String email;
    }
}
