package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("student_task_record")
public class StudentTaskRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("sid")
    private String sid;
    @TableField("taskId")
    private String taskId;
    @TableField("isDone")
    private boolean isDone;
    @TableField("score")
    private int score;
    @TableField("submitTime")
    private Date submitTime;
    @TableField("content")
    private String content;
    @TableField("title")
    private String title;
}
