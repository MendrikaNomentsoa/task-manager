package com.taskmanager.controller;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.*;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task task;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(1L)
                .title("Page accueil")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .build();
    }

    @Test
    void create_shouldReturn201() {
        when(taskService.create(any(), eq(1L), isNull())).thenReturn(task);

        var result = taskController.create(task, 1L, null);

        assertThat(result.getStatusCode().value()).isEqualTo(201);
        assertThat(result.getBody().getTitle()).isEqualTo("Page accueil");
    }

    @Test
    void getByProject_shouldReturnList() {
        when(taskService.findByProject(1L)).thenReturn(List.of(task));

        var result = taskController.getByProject(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    void getById_shouldReturnTask() {
        when(taskService.findById(1L)).thenReturn(task);

        var result = taskController.getById(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(taskService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Tâche non trouvée : 99"));

        assertThatThrownBy(() -> taskController.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateStatus_shouldReturnUpdated() {
        task.setStatus(TaskStatus.DONE);
        when(taskService.updateStatus(1L, TaskStatus.DONE)).thenReturn(task);

        var result = taskController.updateStatus(1L, TaskStatus.DONE);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void delete_shouldReturn204() {
        doNothing().when(taskService).delete(1L);

        var result = taskController.delete(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(204);
    }
}