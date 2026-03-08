package com.example.taskmaster_backend.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileStorageUtil {
    private static final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {

        File dir = new File(UPLOAD_DIR);

        if (!dir.exists())
            dir.mkdirs();

        String filePath = UPLOAD_DIR + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        file.transferTo(new File(filePath));

        return filePath;
    }

}
