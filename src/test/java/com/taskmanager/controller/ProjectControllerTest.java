package com.taskmanager.controller;

import com.taskmanager.model.Project;
import com.taskmanager.service.ProjectService;
import com.taskmanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// On teste directement le Controller sans Spring
@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private Project project;

    @BeforeEach
    void setUp() {
        project = Project.builder()
                .id(1L)
                .name("Refonte site")
                .description("Mon projet")
                .build();
    }

    @Test
    void create_shouldReturnProject() {
        when(projectService.create(any())).thenReturn(project);

        var result = projectController.create(project);

        assertThat(result.getStatusCode().value()).isEqualTo(201);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getName()).isEqualTo("Refonte site");
    }

    @Test
    void findAll_shouldReturnList() {
        when(projectService.findAll()).thenReturn(List.of(project));

        var result = projectController.findAll();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    void findById_shouldReturnProject() {
        when(projectService.findById(1L)).thenReturn(project);

        var result = projectController.findById(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(projectService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Projet non trouvé : 99"));

        assertThatThrownBy(() -> projectController.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void search_shouldReturnList() {
        when(projectService.search("refonte")).thenReturn(List.of(project));

        var result = projectController.search("refonte");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    void update_shouldReturnUpdated() {
        Project updated = Project.builder()
                .id(1L).name("Nouveau nom").build();
        when(projectService.update(eq(1L), any())).thenReturn(updated);

        var result = projectController.update(1L, updated);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getName()).isEqualTo("Nouveau nom");
    }

    @Test
    void delete_shouldReturn204() {
        doNothing().when(projectService).delete(1L);

        var result = projectController.delete(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(204);
    }
}