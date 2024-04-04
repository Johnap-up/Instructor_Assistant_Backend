package org.example.entity.vo.request.saveDataVO;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DetailSaveVO {
    @Length(max = 11)
    String phone;
}
