package org.example.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class TaskCreateVO {
    @Min(1)
    private int type;
    @Length(min = 1, max = 30)
    String title;
    JSONObject content;
    Date issueTime;
    Date endTime;
}
