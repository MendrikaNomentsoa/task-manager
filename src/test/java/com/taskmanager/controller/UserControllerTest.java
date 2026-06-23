package com.taskmanager.controller;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.User;
import com.taskmanager.service.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Jean Dupont")
                .email("jean@email.com")
                .build();
    }

    @Test
    void create_shouldReturn201() {
        when(userService.create(any())).thenReturn(user);

        var result = userController.create(user);

        assertThat(result.getStatusCode().value()).isEqualTo(201);
        assertThat(result.getBody().getName()).isEqualTo("Jean Dupont");
    }

    @Test
    void findAll_shouldReturnList() {
        when(userService.findAll()).thenReturn(List.of(user));

        var result = userController.findAll();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    void findById_shouldReturnUser() {
        when(userService.findById(1L)).thenReturn(user);

        var result = userController.findById(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(userService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Utilisateur non trouvé : 99"));

        assertThatThrownBy(() -> userController.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_shouldReturnUpdated() {
        User updated = User.builder()
                .id(1L).name("Jean Modifié").email("jean@email.com").build();
        when(userService.update(eq(1L), any())).thenReturn(updated);

        var result = userController.update(1L, updated);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getName()).isEqualTo("Jean Modifié");
    }

    @Test
    void delete_shouldReturn204() {
        doNothing().when(userService).delete(1L);

        var result = userController.delete(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(204);
    }
}