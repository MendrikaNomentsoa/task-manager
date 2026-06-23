package com.taskmanager.service;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Jean Dupont")
                .email("jean@email.com")
                .password("secret123")
                .build();
    }

    @Test
    void create_shouldReturnSavedUser() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Jean Dupont");
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void create_shouldThrowException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email déjà utilisé");

        verify(userRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Utilisateur non trouvé");
    }

    @Test
    void findAll_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Jean Dupont");
    }

    @Test
    void delete_shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}