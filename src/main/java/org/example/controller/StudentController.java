package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.entity.RestBean;
import org.example.entity.vo.request.StudentInsertVO;
import org.example.entity.vo.request.saveDataVO.DetailsStudentSaveVO;
import org.example.entity.vo.request.saveDataVO.StudentSavaVO;
import org.example.entity.vo.response.StudentCrudVO;
import org.example.entity.vo.response.StudentDetailVO;
import org.example.entity.vo.response.multDataVO.StudentAllInfoVO;
import org.example.service.AccountService;
import org.example.service.StudentService;
import org.example.utils.ConstUtil;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/student/")
public class StudentController {
    @Resource
    StudentService studentService;
    @Resource
    AccountService accountService;
    @GetMapping("/select-student")      //按需获取学生
    public RestBean<List<StudentCrudVO>> selectStudent(@RequestParam(required = false, value = "name") String name,
                                                       @RequestParam(required = false, value = "classroom") Integer classroom) {
        return RestBean.success(studentService.getSelectStudents(name, classroom));
    }
    @GetMapping("/done-rate")           //每个任务的完成率
    public RestBean<Map<String, String>> getDoneRate(@RequestParam(value = "year") int year,
                                                     @RequestParam(value = "semester") int semester,
                                                     @RequestParam(value = "tid") String tid,
                                                     @RequestParam(value = "type") int type){
        return RestBean.success(studentService.getStudentDoneRate(year, semester, tid, type));
    }
    @GetMapping("/all-info")
    public RestBean<StudentAllInfoVO> getAllInfo(@RequestParam(required = false, value = "name") String name,
                                                 @RequestParam(required = false, value = "classroom") Integer classroom,
                                                 @RequestParam(value = "year") int year,
                                                 @RequestParam(value = "semester") int semester,
                                                 @RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        String tid = accountService.findTidById(id);
        Map<String, String> map1 = studentService.getStudentDoneRate(year, semester, tid, 1);
        Map<String, String> map2 = studentService.getStudentDoneRate(year, semester, tid, 2);
        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("1", map1);
        map.put("2", map2);
        return RestBean.success(new StudentAllInfoVO(studentService.getSelectStudents(name, classroom), map));
    }
    @PostMapping("/delete-student")
    public RestBean<String> deleteStudent(@RequestBody List<String> deleteList, @RequestAttribute(ConstUtil.ATTR_USER_ID) int id) {
        int i = studentService.deleteStudent(deleteList, id);
        return RestBean.success("成功删除" + i + "个学生");
    }
    @PostMapping("/save-student")           //教师端修改学生信息
    public RestBean<String> saveStudent(@RequestBody StudentSavaVO studentSavaVO, @RequestAttribute(ConstUtil.ATTR_USER_ID) int id) {
        String result =studentService.saveStudent(studentSavaVO, id);
        return result == null ? RestBean.success("修改成功") : RestBean.failure(400, result);
    }
    @PostMapping("/insert-student")
    public RestBean<String> insertStudent(@RequestBody @Valid StudentInsertVO vo, @RequestAttribute(ConstUtil.ATTR_USER_ID) int id) {
        String result = studentService.insertStudent(vo, id);
        return result == null ? RestBean.success("添加成功") : RestBean.failure(400, result);
    }
    @GetMapping("/student-detail")
    public RestBean<StudentDetailVO> getStudentDetail(@RequestParam(value = "sid") String sid) {
        return RestBean.success(studentService.getStudentDetail(sid));
    }
    @PostMapping("/save-student-details")       //学生自行修改setting的信息
    public RestBean<String> saveStudentDetails(@RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                               @RequestBody @Valid DetailsStudentSaveVO vo){
        return studentService.studentSettingSave(id, vo) ?
                RestBean.success("保存成功") :
                RestBean.failure(400, "保存失败");
    }
}

//
//