package org.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPreviewVO {
    String taskId;
    String tid;             //发布人的工号
    int type;
    String title;           //标题
    String text;            //文章内容
    List<String> images;    //似乎不需要显示图片，但我先放着
    Date issueTime;         //发布时间
    Date endTime;           //结束时间
    String name;            //发布人名字
    int year;               //学年
    int semester;           //学期
}
