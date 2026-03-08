package com.example.taskmaster_backend.service;

import com.example.taskmaster_backend.entity.Team;
import com.example.taskmaster_backend.entity.User;
import com.example.taskmaster_backend.repository.TeamRepository;
import com.example.taskmaster_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private UserRepository userRepo;

    public Team createTeam(Team team, Long ownerId) {

        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        team.setOwner(owner);
        team.getMembers().add(owner);

        return teamRepo.save(team);
    }

    public Team addMember(Long teamId, Long userId) {

        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        team.getMembers().add(user);
        return teamRepo.save(team);
    }

}
