package org.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ModifyEmailVO {
    @Email
    String email;
    String code;
}
