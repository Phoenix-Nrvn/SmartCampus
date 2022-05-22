package com.project.smartcampus.util;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author LISHANSHAN
 * @ClassName UploadFile
 * @Description TODO
 * @date 2022/05/2022/5/15 23:03
 */

public class UploadFile {

    private static Map<String, Object> error = new HashMap<>();

    private static Map<String, Object> upload = new HashMap<>();

    private static Map<String, Object> uploadPhoto(MultipartFile photo, String path) {

        int MAX_SIZE = 20971520;
        String originalName = photo.getOriginalFilename();

        // 如果保存文件的路径不存在，就创建一个
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        // 限制上传文件的大小
        if (photo.getSize() > MAX_SIZE) {
            error.put("success", false);
            error.put("msg", "上传的图片大小不能超过20MB呀！");
            return error;
        }

        // 限制上传文件的类型
        String[] suffixes = new String[] {".png", ".PNG", ".jpg", ".JPG",
                ".jpeg", ".JPEG", ".gif", ".GIF", ".bmp", ".BMP"};
        SuffixFileFilter suffixFileFilter = new SuffixFileFilter(suffixes);
        if (!suffixFileFilter.accept(new File(path + originalName))) {
            error.put("success", false);
            error.put("msg", "禁止上传此类型文件，上传图片哟！");
            return error;
        }

        return null;
    }

    public static Map<String, Object> getUploadResult(MultipartFile photo, String dirPath, String portraitPath) {
        if (!photo.isEmpty() && photo.getSize() > 0) {
            String originalName = photo.getOriginalFilename();
            Map<String, Object> error_result = UploadFile.uploadPhoto(photo, dirPath);
            if (error_result != null) {
                return error_result;
            }

            String newPhotoName = UUID.randomUUID() + "__" + originalName;
            try {
                photo.transferTo(new File(dirPath + newPhotoName));
                upload.put("success", true);
                // 将存储头像的路径返回给界面
                upload.put("portrait_path", portraitPath + newPhotoName);
            } catch (IOException e) {
                e.printStackTrace();
                upload.put("success", false);
                upload.put("msg", "上传文件失败! 服务器端发生异常!");
                return upload;
            }
        } else {
            upload.put("success", false);
            upload.put("msg", "上传图片失败! 未找到指定图片!");
        }
        return upload;
    }
}
