package org.example.controller;

import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.RestBean;
import org.example.service.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/image")
public class ObjectController {
    @Resource
    ImageService imageService;
    @GetMapping("/learning/**")
    public void imageFetch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.fetchImage(request, response);
    }
    private void fetchImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = request.getContextPath().substring(7);
        ServletOutputStream stream = response.getOutputStream();
        if (imagePath.length() < 13){
            response.setStatus(404);
            stream.println(RestBean.failure(404, "Not found").toString());
        }else{
            try{
                imageService.fetchImageFromMinio(stream, imagePath);
                response.setHeader("Cache-Control", "max-age=2592000");
            } catch (ErrorResponseException exception){
                if (exception.response().code() == 404){
                    response.setStatus(404);
                    stream.println(RestBean.failure(404, "Not found").toString());
                }else{
                    log.error("从Minio获取图片出现异常："+ exception.getMessage(), exception);
                }
            }
        }
    }
}






















