package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.Account;
import org.example.entity.dto.self.StudentIdEmail;

import java.util.List;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
    @Select("select tid from account_detail where id = #{id}")
    String getTidById(int id);

    @Select("select * from account where username = #{username}")
    Account getAccountByUsername(String username);
    @Select("select username, email from account where role = 'student'")
    List<StudentIdEmail> getAllStudentAccount();
}
