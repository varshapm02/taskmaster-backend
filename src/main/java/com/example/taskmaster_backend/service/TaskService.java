package com.example.taskmaster_backend.service;

import com.example.taskmaster_backend.entity.Task;
import com.example.taskmaster_backend.entity.User;
import com.example.taskmaster_backend.repository.TaskRepository;
import com.example.taskmaster_backend.repository.TeamRepository;
import com.example.taskmaster_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TeamRepository teamRepo;

    public Task createTask(Task task, Long creatorId) {

        User creator = userRepo.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setCreatedBy(creator);
        return taskRepo.save(task);
    }

    public Task updateTask(Long id, Task updates) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (updates.getTitle() != null) task.setTitle(updates.getTitle());
        if (updates.getDescription() != null) task.setDescription(updates.getDescription());
        if (updates.getDueDate() != null) task.setDueDate(updates.getDueDate());
        if (updates.getStatus() != null) task.setStatus(updates.getStatus());
        if (updates.getPriority() != null) task.setPriority(updates.getPriority());

        return taskRepo.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task assignTask(Long taskId, Long userId) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignedTo(user);
        return taskRepo.save(task);
    }

    public Task markAsCompleted(Long id) {
        Task task = getTaskById(id);
        task.setStatus(Task.TaskStatus.COMPLETED);
        return taskRepo.save(task);
    }

    public Page<Task> searchTasks(String query, Task.TaskStatus status,
                                  Long assignedToId, Long teamId, Pageable pageable) {

        Specification<Task> spec = (root, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null && !query.isEmpty()) {
                predicates.add(
                        cb.or(
                                cb.like(root.get("title"), "%" + query + "%"),
                                cb.like(root.get("description"), "%" + query + "%")
                        )
                );
            }

            if (status != null)
                predicates.add(cb.equal(root.get("status"), status));

            if (assignedToId != null)
                predicates.add(cb.equal(root.get("assignedTo").get("id"), assignedToId));

            if (teamId != null)
                predicates.add(cb.equal(root.get("team").get("id"), teamId));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return taskRepo.findAll(spec, pageable);
    }

    public List<Task> getTasksByUser(Long userId) {
        return taskRepo.findByAssignedToId(userId);
    }

    public void deleteTask(Long id) {
        taskRepo.deleteById(id);
    }

}
