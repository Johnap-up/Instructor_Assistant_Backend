package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("check_room_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckRoomRecord {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("dormitory")
    String dormitory;
    @TableField("room")
    String room;
    @TableField("taskId")
    String taskId;
    @TableField("submitTime")
    Date submitTime;
    @TableField("content")
    String content;
    @TableField("title")
    String title;
    @TableField("isDone")
    boolean isDone;
}
