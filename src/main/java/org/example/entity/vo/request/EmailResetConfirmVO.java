package org.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResetConfirmVO {      //邮箱验证码step
    @Email
    String email;
//    @Length(max = 6, min = 6)
    String code;
}
