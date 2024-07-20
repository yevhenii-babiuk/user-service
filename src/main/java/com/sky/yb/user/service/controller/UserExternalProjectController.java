package com.sky.yb.user.service.controller;

import com.sky.yb.user.service.dto.UserExternalProjectDto;
import com.sky.yb.user.service.model.UserExternalProject;
import com.sky.yb.user.service.service.UserExternalProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users", produces = "application/json")
@Tag(name = "User External Project Controller")
@RequiredArgsConstructor
public class UserExternalProjectController {
    private final UserExternalProjectService userExternalProjectService;

    @GetMapping("/{id}/projects")
    public ResponseEntity<List<UserExternalProject>> getUserExternalProject(@PathVariable String id) {
        List<UserExternalProject> projects = userExternalProjectService.getProjects(id);
        return projects == null || projects.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(projects);
    }

    @PostMapping("/{id}/projects")
    public ResponseEntity<UserExternalProject> createUserExternalProject(@PathVariable String id,
                                                                         @RequestBody UserExternalProjectDto userExternalProjectDto) {
        return ResponseEntity.ok(userExternalProjectService.addUserExternalProject(id, userExternalProjectDto));
    }

}


