package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.Student;
import org.example.entity.vo.request.StudentInsertVO;
import org.example.entity.vo.request.saveDataVO.DetailsStudentSaveVO;
import org.example.entity.vo.request.saveDataVO.StudentSavaVO;
import org.example.entity.vo.response.StudentCrudVO;
import org.example.entity.vo.response.StudentDetailVO;

import java.util.List;
import java.util.Map;

public interface StudentService extends IService<Student>{

    List<StudentCrudVO> getSelectStudents(String name, Integer classroom);
    Map<String,String> getStudentDoneRate(int year, int semester, String tid, int type);
    Integer deleteStudent(List<String> deleteList);
    String saveStudent(StudentSavaVO studentSavaVO);
    String insertStudent(StudentInsertVO vo);
    StudentDetailVO getStudentDetail(String sid);
    boolean studentSettingSave(int id, DetailsStudentSaveVO vo);
}
