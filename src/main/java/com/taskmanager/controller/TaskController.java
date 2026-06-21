// controller/TaskController.java
package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Gestion des tâches")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Créer une tâche")
    public ResponseEntity<Task> create(
            @Valid @RequestBody Task task,
            @RequestParam Long projectId,
            @RequestParam(required = false) Long assigneeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.create(task, projectId, assigneeId));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Lister les tâches d'un projet")
    public ResponseEntity<List<Task>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.findByProject(projectId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouver une tâche par ID")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'une tâche")
    public ResponseEntity<Task> updateStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une tâche")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}