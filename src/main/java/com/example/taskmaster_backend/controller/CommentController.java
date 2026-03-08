package com.example.taskmaster_backend.controller;

import com.example.taskmaster_backend.dto.CommentDTO;
import com.example.taskmaster_backend.entity.Comment;
import com.example.taskmaster_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin("*")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentDTO dto) {

        Comment c = commentService.addComment(
                dto.getContent(),
                dto.getTaskId(),
                dto.getUserId()
        );

        return ResponseEntity.ok(c);
    }

}
