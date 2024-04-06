package org.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.entity.dto.Account;
import org.example.entity.dto.ImageStore;
import org.example.mapper.AccountMapper;
import org.example.mapper.ImageStoreMapper;
import org.example.service.ImageService;
import org.example.utils.ConstUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl extends ServiceImpl<ImageStoreMapper, ImageStore> implements ImageService{
    @Resource
    MinioClient minioClient;
    @Resource
    AccountMapper accountMapper;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    @Override
    public String uploadAvatar(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        imageName = "/avatar/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(ConstUtil.BUCKET_INSTRUCTOR)
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try{
            minioClient.putObject(args);
            String oldAvatar = accountMapper.selectById(id).getAvatar();
            if (accountMapper.update(null, Wrappers.<Account>update()
                    .eq("id", id).set("avatar", imageName)) > 0){
                this.deleteOldAvatar(oldAvatar);
                return imageName;
            }else {
                return null;
            }
        }catch (Exception e){
            log.info("图片上传出现问题" + e.getMessage());
            return null;
        }
    }
    @Override
    public void fetchImageFromMinio(OutputStream stream, String image) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(ConstUtil.BUCKET_INSTRUCTOR)
                .object(image)
                .build();
        GetObjectResponse response = minioClient.getObject(args);
        IOUtils.copy(response, stream);
    }
    private void deleteOldAvatar(String avatar) throws Exception{           //删除旧头像用
        if (avatar == null || avatar.isEmpty()) return;
        RemoveObjectArgs remove = RemoveObjectArgs.builder()
                .bucket(ConstUtil.BUCKET_INSTRUCTOR)
                .object(avatar)
                .build();
        minioClient.removeObject(remove);
    }
    @Override               //没有加限流
    public String uploadImage(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        imageName = "/cache/" + format.format(date) + "/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(ConstUtil.BUCKET_INSTRUCTOR)
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(args);
            if (this.save(new ImageStore(null, id, imageName, date))){
                return imageName;
            }else {
                return null;
            }
        }catch (Exception e){
            log.error("图片上传出现问题："+ e.getMessage(), e);
            return null;
        }
    }
}





















