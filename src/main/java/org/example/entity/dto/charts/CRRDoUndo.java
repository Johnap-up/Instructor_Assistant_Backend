package org.example.entity.dto.charts;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("check_room_record")
public class CRRDoUndo {
    @TableField("dormitory")
    String dormitory;
    @TableField("room")
    String room;
}
