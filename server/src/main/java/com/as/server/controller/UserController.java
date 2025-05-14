package com.as.server.controller;

import com.as.server.api.users.UsersApi;
import com.as.server.dto.users.UserDTO;
import com.as.server.dto.users.UserRequest;
import com.as.server.entity.User;
import com.as.server.exception.ConflictException;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController implements UsersApi {

    @Autowired
    private UserService userService;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @PostMapping
    public ResponseEntity<UserDTO> usersCreate(@RequestBody UserRequest request) {
        User user = entityMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User created = userService.create(user);
        return ResponseEntity.status(201).body(entityMapper.toUserDTO(created));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserDTO>> usersList() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(entityMapper::toUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @Override
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> usersUpdate(@PathVariable Integer userId, @RequestBody UserRequest request) {
        User user = entityMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User updated = userService.update(userId, user);
        return ResponseEntity.ok(entityMapper.toUserDTO(updated));
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> usersDelete(@PathVariable Integer userId) {
        if (userService.hasTransactions(userId)) {
            throw new ConflictException("User has associated transactions");
        }
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}