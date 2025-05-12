package com.as.server.ServiceImplTest;

import com.as.server.entity.User;
import com.as.server.repository.UserRepository;
import com.as.server.service.impl.UserServiceImpl;
import com.as.server.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setIsAdmin(false);
    }

    /**
     * shouldSaveUser
     */
    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(user);
    }

    /**
     * shouldReturnUser
     */
    @Test
    void findUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findById(1);
    }

    /**
     * shouldThrowEntityNotFoundException
     */
    @Test
    void findUserById_ThrowEntityNotFoundException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.findById(1));

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(1);
    }

    /**
     * shouldReturnUserList
     */
    @Test
    void findAllUser() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userRepository).findAll();
    }

    /**
     * shouldUpdateUser
     */
    @Test
    void updateUser() {
        User updated = new User();
        updated.setUsername("updateduser");
        updated.setPassword("newpassword");
        updated.setIsAdmin(true);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.update(1, updated);

        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
        assertEquals("newpassword", result.getPassword());
        assertTrue(result.getIsAdmin());
        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }

    /**
     * shouldDeleteUser
     */
    @Test
    void deleteUser() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.delete(1);

        verify(userRepository).existsById(1);
        verify(userRepository).deleteById(1);
    }

    /**
     * shouldThrowEntityNotFoundException
     */
    @Test
    void deleteUser_ThrowEntityNotFoundException() {
        when(userRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.delete(1));

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).existsById(1);
        verify(userRepository, never()).deleteById(1);
    }
}
