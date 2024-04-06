package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("image_store")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageStore {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("uid")
    private int uid;
    @TableField("name")
    private String name;
    @TableField("time")
    private Date time;
}
