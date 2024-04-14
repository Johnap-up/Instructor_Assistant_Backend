package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.AccountDetails;

@Mapper
public interface AccountDetailsMapper extends BaseMapper<AccountDetails> {
    @Select("select * from account_detail where tid = #{tid}")
    AccountDetails selectByTid(String tid);
}
