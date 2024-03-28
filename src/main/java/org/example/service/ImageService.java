package org.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadLearning(MultipartFile file, int id)throws IOException;
}
