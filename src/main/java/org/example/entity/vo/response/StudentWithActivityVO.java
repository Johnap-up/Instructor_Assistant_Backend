package org.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentWithActivityVO {
    private String sid;
    private String name;
    private int gender;
    private String dormitory;
    private String room;
    private int classroom;
    private int type;
    private int doneNum;
    private int totalNum;
}
