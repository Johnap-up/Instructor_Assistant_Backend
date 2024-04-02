package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.Student;
import org.example.entity.vo.response.StudentActivityRate;
import org.example.mapper.StudentMapper;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Resource
    StudentMapper studentMapper;
    @Override
    public List<Student> getSelectStudents(String name, Integer classroom) {
        if (name != null && classroom != null){
            return studentMapper.selectStudentByNameAndClassroom(name, classroom);
        }else if (name != null){
            return studentMapper.selectStudentByName(name);
        }else if (classroom != null) {
            return studentMapper.selectStudentByClassroom(classroom);
        }else{
            return studentMapper.selectAllStudent();
        }
    }

    @Override
    public Map<String, String> getStudentDoneRate(int year, String tid, int type) {
        List<StudentActivityRate> list = studentMapper.getFinishRate(year, tid, type);
        return list.stream().collect(Collectors.toMap(
                StudentActivityRate::getSid,
                studentActivityRate -> studentActivityRate.getDoneNum() + "/" + studentActivityRate.getTotalNum()
        ));
    }
}
