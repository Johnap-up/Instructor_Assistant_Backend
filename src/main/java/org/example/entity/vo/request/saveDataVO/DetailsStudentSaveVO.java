package org.example.entity.vo.request.saveDataVO;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DetailsStudentSaveVO {
    @Length(max = 11)
    String phone;
    String qq;
}
