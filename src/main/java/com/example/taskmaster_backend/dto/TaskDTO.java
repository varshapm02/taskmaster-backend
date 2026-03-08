package com.example.taskmaster_backend.dto;

import com.example.taskmaster_backend.entity.Task;
import com.example.taskmaster_backend.entity.Task.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TaskDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private Task.TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private Long assignedToId;
    private Long teamId;
}
