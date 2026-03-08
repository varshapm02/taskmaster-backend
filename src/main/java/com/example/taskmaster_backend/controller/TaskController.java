package com.example.taskmaster_backend.controller;

import com.example.taskmaster_backend.dto.TaskDTO;
import com.example.taskmaster_backend.entity.Task;
import com.example.taskmaster_backend.entity.User;
import com.example.taskmaster_backend.service.TaskService;
import com.example.taskmaster_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private Long getUserId(Authentication auth) {
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> createTask(Authentication auth, @Valid @RequestBody TaskDTO dto) {

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());

        return ResponseEntity.ok(
                taskService.createTask(task, getUserId(auth))
        );
    }

    @GetMapping
    public ResponseEntity<?> getTasks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Task.TaskStatus status,
            @RequestParam(required = false) Long assignedToId,
            @RequestParam(required = false) Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Task> tasks = taskService.searchTasks(
                query, status, assignedToId, teamId, pageable
        );

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                        @Valid @RequestBody TaskDTO dto) {

        Task updates = new Task();
        updates.setTitle(dto.getTitle());
        updates.setDescription(dto.getDescription());
        updates.setStatus(dto.getStatus());
        updates.setPriority(dto.getPriority());
        updates.setDueDate(dto.getDueDate());

        return ResponseEntity.ok(taskService.updateTask(id, updates));
    }

    @PutMapping("/{id}/assign/{userId}")
    public ResponseEntity<?> assignTask(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignTask(id, userId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markAsCompleted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted");
    }

}
