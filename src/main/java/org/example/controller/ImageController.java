package org.example.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.RestBean;
import org.example.service.ImageService;
import org.example.utils.ConstUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Resource
    ImageService imageService;
    @PostMapping("/avatar")
    public RestBean<String> uploadLearning(@RequestParam("file") MultipartFile file,
                                           @RequestAttribute(ConstUtil.ATTR_USER_ID) int id) throws IOException {
        if (file.getSize() > 1024 * 1024 * 2)
            return RestBean.failure(400, "图像大小不得超过2M");
        log.info("正在进行图像上传操作");
        String url = imageService.uploadAvatar(file, id);
        if (url != null){
            log.info("图片上传成功，大小为" + file.getSize());
            return RestBean.success(url);
        } else{
            return RestBean.failure(400, "图片上传失败，请联系管理员");
        }
    }
}
