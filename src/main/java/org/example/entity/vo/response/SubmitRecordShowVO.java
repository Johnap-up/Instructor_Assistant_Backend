package org.example.entity.vo.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SubmitRecordShowVO {
    int id;                 //提交记录的id
    String content;         //提交记录的内容
    String title;           //标题
    String text;            //文字内容，用于缩略
    List<String> images;    //相关图片
    Date submitTime;        //提交时间
    User user;

    @Data
    public static class User {      //学生的信息
        Integer id;                 //account中的id
        String sid;                 //学号
        String name;                //姓名
        String role;
        String avatar;
        String qq;
        String phone;
        int gender;
        String email;
    }
}
