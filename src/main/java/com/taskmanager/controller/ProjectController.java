package com.taskmanager.controller;

import com.taskmanager.model.Project;
import com.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // POST /api/projects
    @PostMapping
    public ResponseEntity<Project> create(@Valid @RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.create(project));
    }

    // GET /api/projects
    @GetMapping
    public ResponseEntity<List<Project>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    // GET /api/projects/1
    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    // GET /api/projects/search?name=refonte
    @GetMapping("/search")
    public ResponseEntity<List<Project>> search(@RequestParam String name) {
        return ResponseEntity.ok(projectService.search(name));
    }

    // PUT /api/projects/1
    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id,
                                          @Valid @RequestBody Project project) {
        return ResponseEntity.ok(projectService.update(id, project));
    }

    // DELETE /api/projects/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}