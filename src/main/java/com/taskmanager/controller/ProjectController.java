// controller/ProjectController.java
package com.taskmanager.controller;

import com.taskmanager.model.Project;
import com.taskmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Gestion des projets")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Créer un projet")
    public ResponseEntity<Project> create(@Valid @RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.create(project));
    }

    @GetMapping
    @Operation(summary = "Lister tous les projets")
    public ResponseEntity<List<Project>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouver un projet par ID")
    public ResponseEntity<Project> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher un projet par nom")
    public ResponseEntity<List<Project>> search(@RequestParam String name) {
        return ResponseEntity.ok(projectService.search(name));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un projet")
    public ResponseEntity<Project> update(@PathVariable Long id,
                                          @Valid @RequestBody Project project) {
        return ResponseEntity.ok(projectService.update(id, project));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un projet")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}