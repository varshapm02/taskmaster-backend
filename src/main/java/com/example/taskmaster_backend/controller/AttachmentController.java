package com.example.taskmaster_backend.controller;
import com.example.taskmaster_backend.entity.Attachment;
import com.example.taskmaster_backend.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachments")
@CrossOrigin("*")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload/{taskId}/{userId}")
    public ResponseEntity<?> upload(
            @PathVariable Long taskId,
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        Attachment saved = attachmentService.uploadAttachment(taskId, userId, file);

        return ResponseEntity.ok(saved);
    }

}
