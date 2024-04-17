package org.example.entity.dto.charts;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.reflection.BaseData;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("check_room_record")
public class CRRDoUndo implements BaseData {
    @TableField("dormitory")
    String dormitory;
    @TableField("room")
    String room;
}
