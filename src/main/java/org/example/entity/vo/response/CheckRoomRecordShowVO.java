package org.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckRoomRecordShowVO {
    int id;                     //checkRoomRecord表中的id
    String content;
    String title;
    String text;
    List<String> images;
    Date submitTime;
    User user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User{
        String dormitory;
        String room;
        List<String> names;
    }
}
