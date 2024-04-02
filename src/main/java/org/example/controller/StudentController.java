package org.example.controller;

import jakarta.annotation.Resource;
import org.example.entity.RestBean;
import org.example.entity.dto.Student;
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
    public RestBean<List<Student>> selectStudent(@RequestParam(required = false, value = "name") String name,
                                                 @RequestParam(required = false, value = "classroom") Integer classroom) {
        return RestBean.success(studentService.getSelectStudents(name, classroom));
    }
    @GetMapping("/done-rate")           //每个任务的完成率
    public RestBean<Map<String, String>> getDoneRate(@RequestParam(value = "year") int year,
                                                     @RequestParam(value = "tid") String tid,
                                                     @RequestParam(value = "type") int type){
        return RestBean.success(studentService.getStudentDoneRate(year, tid, type));
    }
    @GetMapping("/all-info")
    public RestBean<StudentAllInfoVO> getAllInfo(@RequestParam(required = false, value = "name") String name,
                                                 @RequestParam(required = false, value = "classroom") Integer classroom,
                                                 @RequestParam(value = "year") int year,
                                                 @RequestAttribute(ConstUtil.ATTR_USER_ID) int id){
        String tid = accountService.findTidById(id);
        Map<String, String> map1 = studentService.getStudentDoneRate(year, tid, 1);
        Map<String, String> map2 = studentService.getStudentDoneRate(year, tid, 2);
        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("1", map1);
        map.put("2", map2);
        return RestBean.success(new StudentAllInfoVO(studentService.getSelectStudents(name, classroom), map));
    }
    @PostMapping("/delete-student")
    public RestBean<String> deleteStudent(@RequestBody List<String> deleteList) {
        System.out.println(deleteList);
        return RestBean.success("hello");
    }
}

//
//