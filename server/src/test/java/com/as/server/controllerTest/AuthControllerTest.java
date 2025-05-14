package com.as.server.controllerTest;

import com.as.server.controller.AuthController;
import com.as.server.dto.auth.LoginRequest;
import com.as.server.dto.auth.LoginResponse;
import com.as.server.entity.User;
import com.as.server.enums.Role;
import com.as.server.security.JwtUtil;
import com.as.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setIsAdmin(true);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
    }

    @Test
    void authLogin_successfulAdminLogin_returnsTokenAndRole() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(1, "ADMIN")).thenReturn("jwt.token.here");

        // Act
        ResponseEntity<LoginResponse> response = authController.authLogin(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        LoginResponse loginResponse = response.getBody();
        assertEquals("jwt.token.here", loginResponse.getToken());
        assertEquals(Role.ADMIN, loginResponse.getRole());
        verify(userService).findByUsername("testuser");
        verify(passwordEncoder).matches("password", "encodedPassword");
        verify(jwtUtil).generateToken(1, "ADMIN");
    }

    @Test
    void authLogin_invalidCredentials_returnsUnauthorized() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        // Act
        ResponseEntity<LoginResponse> response = authController.authLogin(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).findByUsername("testuser");
        verify(passwordEncoder).matches("password", "encodedPassword");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void authLogin_userNotFound_returnsUnauthorized() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(null);

        // Act
        ResponseEntity<LoginResponse> response = authController.authLogin(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).findByUsername("testuser");
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void authLogin_nullRequest_returnsUnauthorized() {
        // Act
        ResponseEntity<LoginResponse> response = authController.authLogin(null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verifyNoInteractions(userService, passwordEncoder, jwtUtil);
    }

    @Test
    void authLogin_emptyUsername_returnsUnauthorized() {
        // Arrange
        loginRequest.setUsername("");

        // Act
        ResponseEntity<LoginResponse> response = authController.authLogin(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verifyNoInteractions(userService, passwordEncoder, jwtUtil);
    }

    @Test
    void authLogin_emptyPassword_returnsUnauthorized() {
        // Arrange
        loginRequest.setPassword("");

        // Act
        ResponseEntity<LoginResponse> response = authController.authLogin(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verifyNoInteractions(userService, passwordEncoder, jwtUtil);
    }
}