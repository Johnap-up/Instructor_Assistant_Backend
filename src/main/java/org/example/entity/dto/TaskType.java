package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.example.entity.reflection.BaseData;

@TableName("task_type")
@Data
public class TaskType implements BaseData {
    @TableId(type = IdType.INPUT)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("`desc`")
    private String desc;
}
