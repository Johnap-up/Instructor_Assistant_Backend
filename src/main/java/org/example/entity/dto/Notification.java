package org.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.reflection.BaseData;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("notification")
public class Notification implements BaseData {
    @TableId(type = IdType.AUTO)
    Integer id;
    String tid;             //暂时不镐删除，
    String title;
    String content;
    Date time;
    String type;
    String url;
}
