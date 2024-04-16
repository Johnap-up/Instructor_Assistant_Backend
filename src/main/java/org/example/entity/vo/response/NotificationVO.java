package org.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationVO {
    int id;
    String tid;
    String title;
    String content;
    Date time;
    String type;
    String url;
}
