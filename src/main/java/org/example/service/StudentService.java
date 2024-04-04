package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Student;
import org.example.entity.vo.request.saveDataVO.StudentSavaVO;

import java.util.List;
import java.util.Map;

public interface StudentService extends IService<Student>{

    List<Student> getSelectStudents(String name, Integer classroom);
    Map<String,String> getStudentDoneRate(int year, String tid, int type);
    Integer deleteStudent(List<String> deleteList);
    String saveStudent(StudentSavaVO studentSavaVO);
    String insertStudent(Student student);
}
