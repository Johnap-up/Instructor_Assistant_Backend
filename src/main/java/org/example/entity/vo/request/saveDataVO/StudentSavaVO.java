package org.example.entity.vo.request.saveDataVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.reflection.BaseData;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSavaVO implements BaseData {
    private String sid;     //new
    private String oldSid;
    private String name;
    private int gender;
    private String dormitory;
    private String room;
    private int classroom;
    private String qq;
    private String phone;
}
