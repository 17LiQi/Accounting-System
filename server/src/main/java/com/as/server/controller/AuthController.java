package com.as.server.controller;

import com.as.server.dto.auth.LoginRequest;
import com.as.server.dto.auth.LoginResponse;
import com.as.server.dto.error.ApiError;
import com.as.server.entity.User;
import com.as.server.enums.Role;
import com.as.server.security.JwtUtil;
import com.as.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            ApiError error = new ApiError();
            error.setCode(ApiError.CodeEnum.valueOf("INVALID_CREDENTIALS"));
            error.setMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String role = user.getIsAdmin() ? "ADMIN" : "USER";
        String token = jwtUtil.generateToken(user.getUserId(), role);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRole(Role.valueOf(role));
        return ResponseEntity.ok(response);
    }
}
