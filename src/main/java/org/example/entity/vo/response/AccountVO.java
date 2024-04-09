package org.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {
    private String email;
    private String username;
    private String name;
    private String role;
    private Date registerTime;
    private String avatar;
}
