package com.example.taskmaster_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String profilePicture;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Task> assignedTasks = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Task> createdTasks = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
