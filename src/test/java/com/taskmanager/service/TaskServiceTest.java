package com.taskmanager.service;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.*;
import com.taskmanager.repository.*;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Project project;
    private User user;

    @BeforeEach
    void setUp() {
        project = Project.builder().id(1L).name("Refonte site").build();
        user = User.builder().id(1L).name("Jean").email("jean@email.com").build();
        task = Task.builder()
                .id(1L)
                .title("Page accueil")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .project(project)
                .build();
    }

    @Test
    void create_shouldReturnTask_withProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any())).thenReturn(task);

        Task result = taskService.create(task, 1L, null);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Page accueil");
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void create_shouldThrowException_whenProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(task, 99L, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Projet non trouvé");
    }

    @Test
    void updateStatus_shouldChangeStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        Task result = taskService.updateStatus(1L, TaskStatus.DONE);

        assertThat(result).isNotNull();
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void findByProject_shouldReturnTasks() {
        when(taskRepository.findByProjectId(1L)).thenReturn(List.of(task));

        List<Task> result = taskService.findByProject(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Page accueil");
    }

    @Test
    void delete_shouldDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.delete(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}