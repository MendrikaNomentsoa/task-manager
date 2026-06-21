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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// @ExtendWith = utilise Mockito pour simuler les dépendances
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // @Mock = simule le repository, pas besoin de vraie base de données
    @Mock
    private UserRepository userRepository;

    // @InjectMocks = crée le service avec les mocks injectés
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Crée un utilisateur de test avant chaque test
        user = User.builder()
                .id(1L)
                .name("Jean Dupont")
                .email("jean@email.com")
                .build();
    }

    @Test
    void create_shouldReturnSavedUser() {
        // GIVEN : le repository retourne false pour existsByEmail
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        // WHEN : on appelle le service
        User result = userService.create(user);

        // THEN : on vérifie le résultat
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Jean Dupont");
        assertThat(result.getEmail()).isEqualTo("jean@email.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void create_shouldThrowException_whenEmailAlreadyExists() {
        // GIVEN : email déjà utilisé
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // THEN : doit lancer une exception
        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email déjà utilisé");

        verify(userRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnUser() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // WHEN
        User result = userService.findById(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        // GIVEN
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // THEN
        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Utilisateur non trouvé");
    }

    @Test
    void findAll_shouldReturnListOfUsers() {
        // GIVEN
        when(userRepository.findAll()).thenReturn(List.of(user));

        // WHEN
        List<User> result = userService.findAll();

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Jean Dupont");
    }

    @Test
    void delete_shouldDeleteUser() {
        // GIVEN
        when(userRepository.existsById(1L)).thenReturn(true);

        // WHEN
        userService.delete(1L);

        // THEN
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        // GIVEN
        when(userRepository.existsById(99L)).thenReturn(false);

        // THEN
        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}