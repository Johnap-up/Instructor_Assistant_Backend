package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.RestBean;
import org.example.entity.dto.excel.CRRDoUndoExcel;
import org.example.entity.dto.excel.STRDoUndoExcel;
import org.example.entity.dto.excel.StudentRate;
import org.example.entity.vo.response.StudentCrudVO;
import org.example.service.*;
import org.example.utils.ConstUtil;
import org.example.utils.excel.ExcelExportHandle;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    @Resource
    private StudentExcelService studentExcelService;
    @Resource
    private ExcelExportHandle excelExportHandle;
    @Resource
    StudentService studentService;
    @Resource
    AccountService accountService;
    @Resource
    ExcelService excelService;
    @Resource
    LogService logService;

    @PostMapping("/student")
    public RestBean<String> importStudentExcel(@RequestParam("files") MultipartFile multipartFile){
        studentExcelService.importExcel(multipartFile);
        return RestBean.success("ok");
    }
    @GetMapping("/downloadAllStudents")
    public void downloadAllStudents(HttpServletResponse response,
                      @RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                      @RequestParam(required = false, value = "name") String name,
                      @RequestParam(required = false, value = "classroom") Integer classroom){
        //从数据库中取出导出的数据
        excelExportHandle.export(response, "用户表", getStudentRates(id, name, classroom), StudentRate.class);
        logService.insertLog(id, "下载了学生表");
    }
    @GetMapping("/download-CRRDoUndo")
    public void downloadCRRDoUndo(HttpServletResponse response,
                                  @RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                  @RequestParam("taskId") String taskId){
        excelExportHandle.export(response, "查寝完成率表", excelService.getCRRDoUndo(taskId), CRRDoUndoExcel.class);
        logService.insertLog(id, "下载了查寝完成率表");
    }
    @GetMapping("/download-STRDoUndo")
    public void downloadSTRDoUndo(HttpServletResponse response,
                                  @RequestAttribute(ConstUtil.ATTR_USER_ID) int id,
                                  @RequestParam("taskId") String taskId){
        excelExportHandle.export(response, "完成率表", excelService.getSTRDoUndo(taskId), STRDoUndoExcel.class);
        logService.insertLog(id, "下载了完成率表");
    }
    private List<StudentRate> getStudentRates(int id, String name, Integer classroom){
        String tid = accountService.findTidById(id);
        Map<String, String> map1 = studentService.getStudentDoneRate(2023, 2, tid, 1);
        Map<String, String> map2 = studentService.getStudentDoneRate(2023, 2, tid, 2);
        List<StudentCrudVO> s =studentService.getSelectStudents(name, classroom);
        List<StudentRate> list2 = new ArrayList<>();
        StudentCrudVO studentCrudVO;
        for (StudentCrudVO crudVO : s) {
            StudentRate ss = new StudentRate();
            studentCrudVO = crudVO;
            BeanUtils.copyProperties(studentCrudVO, ss);
            ss.setRate1(map1.get(studentCrudVO.getSid()));
            ss.setRate2(map2.get(studentCrudVO.getSid()));
            list2.add(ss);
        }
        return list2;
    }
}
