package org.example.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Date;

@Data
public class TaskUpdateVO {
    String taskId;
    @Min(1)
    @Max(3)
    private int type;
    String title;
    JSONObject content;
    Date endTime;
    Date issueTime;
}
