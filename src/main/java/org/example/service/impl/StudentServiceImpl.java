package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.Student;
import org.example.entity.vo.request.saveDataVO.StudentSavaVO;
import org.example.entity.vo.response.StudentActivityRate;
import org.example.mapper.StudentMapper;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @Override
    public Integer deleteStudent(List<String> deleteList) {
        return studentMapper.deleteBatchIds(deleteList);
    }
    @Override
    public synchronized String saveStudent(StudentSavaVO studentSavaVO) {
        String sid = studentSavaVO.getSid();
        String oldSid = studentSavaVO.getOldSid();
        Student student = studentMapper.selectById(sid);
        if (!Objects.equals(oldSid, sid) && student != null)
            return "目标学号已存在，请重新设置";
        if (student == null){           //修改sid后没有发生冲突
            this.update().eq("sid", oldSid).set("sid", sid).update();
        }
        this.updateById(studentSavaVO.asViewObject(Student.class));
        return null;
    }
    @Override
    public String insertStudent(Student student) {
        String sid = student.getSid();
        Student s = studentMapper.selectById(sid);
        if (s != null)
            return "目标学号已存在，请重新设置";
        this.save(student);
        return null;
    }
}
