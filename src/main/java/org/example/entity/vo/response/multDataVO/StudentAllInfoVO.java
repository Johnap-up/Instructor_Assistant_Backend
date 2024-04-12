package org.example.entity.vo.response.multDataVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.vo.response.StudentCrudVO;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAllInfoVO {
    List<StudentCrudVO> studentList;
    Map<String, Map<String, String>> map;
}
