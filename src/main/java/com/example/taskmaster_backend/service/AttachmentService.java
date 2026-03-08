package com.example.taskmaster_backend.service;

import com.example.taskmaster_backend.entity.Attachment;
import com.example.taskmaster_backend.entity.Task;
import com.example.taskmaster_backend.entity.User;
import com.example.taskmaster_backend.repository.AttachmentRepository;
import com.example.taskmaster_backend.repository.TaskRepository;
import com.example.taskmaster_backend.repository.UserRepository;
import com.example.taskmaster_backend.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FileStorageUtil fileStorage;

    public Attachment uploadAttachment(Long taskId, Long userId, MultipartFile file) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String path = fileStorage.saveFile(file);

            Attachment attachment = new Attachment();
            attachment.setTask(task);
            attachment.setUploadedBy(user);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFilePath(path);
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());

            return attachmentRepo.save(attachment);

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

}
