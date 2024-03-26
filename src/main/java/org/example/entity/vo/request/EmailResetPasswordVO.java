package org.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailResetPasswordVO {     //重置密码step
    @Email
    @NotNull
    private String email;
    @Length(min = 6, max = 16)
    private String password;
}
