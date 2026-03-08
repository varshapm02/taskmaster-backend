package com.example.taskmaster_backend.controller;
import com.example.taskmaster_backend.dto.TeamDTO;
import com.example.taskmaster_backend.entity.Team;
import com.example.taskmaster_backend.entity.User;
import com.example.taskmaster_backend.service.TeamService;
import com.example.taskmaster_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin("*")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    private Long getUserId(Authentication auth) {
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> createTeam(Authentication auth, @RequestBody TeamDTO dto) {

        Team team = new Team();
        team.setName(dto.getName());
        team.setDescription(dto.getDescription());

        return ResponseEntity.ok(
                teamService.createTeam(team, getUserId(auth))
        );
    }

    @PostMapping("/{teamId}/add-member/{userId}")
    public ResponseEntity<?> addMember(@PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(teamService.addMember(teamId, userId));
    }

}
