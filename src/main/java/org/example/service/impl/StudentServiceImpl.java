package org.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.AccountDetails;
import org.example.entity.dto.Student;
import org.example.entity.vo.request.StudentInsertVO;
import org.example.entity.vo.request.saveDataVO.DetailsStudentSaveVO;
import org.example.entity.vo.request.saveDataVO.StudentSavaVO;
import org.example.entity.vo.response.StudentDetailVO;
import org.example.entity.vo.response.StudentTaskRate;
import org.example.entity.vo.response.StudentCrudVO;
import org.example.mapper.AccountDetailsMapper;
import org.example.mapper.AccountMapper;
import org.example.mapper.StudentMapper;
import org.example.service.AccountService;
import org.example.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Resource
    StudentMapper studentMapper;
    @Resource
    AccountMapper accountMapper;
    @Resource
    AccountService accountService;
    @Resource
    AccountDetailsMapper accountDetailsMapper;
    @Override
    public List<StudentCrudVO> getSelectStudents(String name, Integer classroom) {
        System.out.println(name + ", class: "+classroom);
        Map<String, String> map = accountService.getAllStudentIdEmail();
        if (name != null && classroom != null){
            return studentMapper.selectStudentByNameAndClassroom(name, classroom).stream()
                    .map(student -> student.asViewObject(StudentCrudVO.class, vo -> vo.setEmail(map.get(student.getSid())))).toList();
        }else if (name != null){
            return studentMapper.selectStudentByName(name).stream()
                    .map(student -> student.asViewObject(StudentCrudVO.class, vo -> vo.setEmail(map.get(student.getSid())))).toList();
        }else if (classroom != null) {
            return studentMapper.selectStudentByClassroom(classroom).stream()
                    .map(student -> student.asViewObject(StudentCrudVO.class, vo -> vo.setEmail(map.get(student.getSid())))).toList();
        }else{
            return studentMapper.selectAllStudent().stream()
                    .map(student -> student.asViewObject(StudentCrudVO.class, vo -> vo.setEmail(map.get(student.getSid())))).toList();
        }
    }
    @Override
    public Map<String, String> getStudentDoneRate(int year, int semester, String tid, int type) {       //即使没提交也能有数据
        List<StudentTaskRate> list = studentMapper.getFinishRate(year, semester, tid, type);
        int totalNum = studentMapper.getTotalNum(year, semester, tid, type);
        List<String> sidList = studentMapper.selectAllSid();
        Map<String, String> map2 = list.stream().collect(Collectors.toMap(
                StudentTaskRate::getSid,
                studentTaskRate -> studentTaskRate.getDoneNum() + "/" + studentTaskRate.getTotalNum()
        ));
        Map<String, String> map = sidList.stream().collect(Collectors.toMap(key->key, value->"0/" + totalNum));
        map.putAll(map2);
        return map;
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
    public String insertStudent(StudentInsertVO vo) {
        String sid = vo.getSid();
        Student s = studentMapper.selectById(sid);
        if (s != null)
            return "目标学号已存在，请重新设置";
        Student student = new Student();
        BeanUtils.copyProperties(vo, student);
        this.save(student);
        return null;
    }

    @Override
    public StudentDetailVO getStudentDetail(String sid) {
        Student student = studentMapper.selectById(sid);
        String tid = student.getTid();
        AccountDetails accountDetails = accountDetailsMapper.selectOne(Wrappers.<AccountDetails>query().eq("tid", tid));
        return student.asViewObject(StudentDetailVO.class, vo -> vo.setInstructor(accountDetails.asViewObject(StudentDetailVO.Instructor.class, instructor ->{
            instructor.setTid(accountDetails.getTid());
            instructor.setName(accountDetails.getName());
            instructor.setPhone(accountDetails.getPhone());
        })));
    }

    @Override
    public boolean studentSettingSave(int id, DetailsStudentSaveVO vo) {
        return this.update().eq("sid", accountMapper.selectById(id).getUsername())
                .set("phone", vo.getPhone())
                .set("qq", vo.getQq()).update();
    }
}
