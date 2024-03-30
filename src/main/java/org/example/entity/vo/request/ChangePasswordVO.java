package org.example.entity.vo.request;

import lombok.Data;

@Data
public class ChangePasswordVO {
    String password;
    String new_password;
    String new_password_repeat;
}
