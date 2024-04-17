package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.dto.excel.StudentExcel;
import org.springframework.web.multipart.MultipartFile;

public interface StudentExcelService extends IService<StudentExcel> {
    void importExcel(MultipartFile multipartFile);
}
