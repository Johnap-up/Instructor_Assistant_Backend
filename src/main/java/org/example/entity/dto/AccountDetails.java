package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.reflection.BaseData;

@Data
@TableName("account_detail")
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails implements BaseData {
    @TableId
    Integer id;
    @TableField("name")
    String name;
    @TableField("gender")
    int gender;
    @TableField("institute")
    String institute;
    @TableField("grade")
    String grade;
    @TableField("annex_class")
    String annex_class;
    @TableField("experience")
    String experience;
    @TableField("phone")
    String phone;
    @TableField("tid")
    String tid;
}
