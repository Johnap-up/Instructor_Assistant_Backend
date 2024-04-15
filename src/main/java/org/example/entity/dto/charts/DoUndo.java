package org.example.entity.dto.charts;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("student")
public class DoUndo {
    @TableId(type = IdType.INPUT)
    String sid;
    @TableField("id")
    int id;
    @TableField("name")
    String name;
    @TableField("qq")
    String qq;
}
