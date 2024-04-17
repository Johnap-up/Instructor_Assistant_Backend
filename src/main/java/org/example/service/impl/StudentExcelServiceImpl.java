package org.example.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.excel.StudentExcel;
import org.example.listener.ImportDataListener;
import org.example.mapper.excel.BatchInsertMapper;
import org.example.mapper.excel.StudentXMLMapper;
import org.example.service.StudentExcelService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class StudentExcelServiceImpl extends ServiceImpl<StudentXMLMapper, StudentExcel> implements StudentExcelService {
    @Resource
    StudentXMLMapper studentXMLMapper;
    @Override
    public void importExcel(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            EasyExcel.read(inputStream, StudentExcel.class, new ImportDataListener<StudentExcel>() {
                @Override
                protected BatchInsertMapper<StudentExcel> getMapper() {
                    return studentXMLMapper;
                }
            }).sheet().doRead();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
