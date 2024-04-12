package org.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailVO {
    String taskId;
    int type;
    String title;
    String content;
    Date issueTime;
    Date endTime;
    int year;
    int semester;
    Long recordAmount;
    User user;
    @Data
    public static class User {      //这里的User是发帖人的信息，也就是显示account和account_detail
        Integer id;
        String name;
        String role;
        String avatar;
        String phone;
        int gender;
        String email;
    }
}
