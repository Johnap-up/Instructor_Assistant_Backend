package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.reflection.BaseData;

import java.util.Date;

@Data
@TableName("task")
@AllArgsConstructor
@NoArgsConstructor
public class Task implements BaseData {
    @TableId(type = IdType.INPUT)
    private String taskId;
    @TableField("tid")
    private String tid;
    @TableField("type")
    private int type;
    @TableField("sequence")
    private int sequence;
    @TableField("title")
    private String title;
    @TableField("content")
    private String content;
    @TableField("issue_time")
    private Date issueTime;
    @TableField("end_time")
    private Date endTime;
    @TableField("year")
    private int year;
    @TableField("semester")
    private int semester;
}
