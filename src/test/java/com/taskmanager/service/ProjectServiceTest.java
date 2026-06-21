package com.taskmanager.service;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Project;
import com.taskmanager.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

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
    void create_shouldReturnSavedProject() {
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.create(project);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Refonte site");
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void findById_shouldReturnProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Projet non trouvé");
    }

    @Test
    void update_shouldUpdateProject() {
        Project updated = Project.builder()
                .name("Nouveau nom")
                .description("Nouvelle description")
                .build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any())).thenReturn(project);

        Project result = projectService.update(1L, updated);

        assertThat(result).isNotNull();
        verify(projectRepository, times(1)).save(any());
    }

    @Test
    void findAll_shouldReturnList() {
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<Project> result = projectService.findAll();

        assertThat(result).hasSize(1);
    }
}