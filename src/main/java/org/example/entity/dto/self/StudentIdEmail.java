package org.example.entity.dto.self;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account")
public class StudentIdEmail {
    @TableField("username")
    public String username;
    @TableField("email")
    public String email;
}
