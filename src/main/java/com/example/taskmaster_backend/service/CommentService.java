package com.example.taskmaster_backend.service;

import com.example.taskmaster_backend.entity.Comment;
import com.example.taskmaster_backend.entity.Task;
import com.example.taskmaster_backend.entity.User;
import com.example.taskmaster_backend.repository.CommentRepository;
import com.example.taskmaster_backend.repository.TaskRepository;
import com.example.taskmaster_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    public Comment addComment(String content, Long taskId, Long userId) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTask(task);
        comment.setUser(user);

        return commentRepo.save(comment);
    }

}
