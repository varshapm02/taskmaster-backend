package com.example.taskmaster_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDTO {
    @NotBlank(message = "Comment content cannot be empty")
    private String content;

    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotNull(message = "User ID is required")
    private Long userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
