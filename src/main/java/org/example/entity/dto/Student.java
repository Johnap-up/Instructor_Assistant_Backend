package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.reflection.BaseData;

@Data
@TableName("student")
@AllArgsConstructor
@NoArgsConstructor
public class Student implements BaseData {
    @TableId(type = IdType.INPUT)
    private String sid;
    @TableField("name")
    private String name;
    @TableField("gender")
    private int gender;
    @TableField("dormitory")
    private String dormitory;
    @TableField("room")
    private String room;
    @TableField("classroom")
    private int classroom;
    @TableField("qq")
    private String qq;
    @TableField("phone")
    private String phone;
    @TableField("institute")
    private String institute;
    @TableField("grade")
    private int grade;
    @TableField("tid")
    private String tid;
}
