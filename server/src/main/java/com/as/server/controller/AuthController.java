package com.as.server.controller;

import com.as.server.api.auth.AuthApi;
import com.as.server.dto.auth.LoginRequest;
import com.as.server.dto.auth.LoginResponse;
import com.as.server.dto.error.ApiError;
import com.as.server.entity.User;
import com.as.server.enums.Role;
import com.as.server.security.JwtUtil;
import com.as.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController implements AuthApi {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @PostMapping
    public ResponseEntity<LoginResponse> authLogin(@RequestBody LoginRequest request) {
        log.info("收到登录请求: username={}", request != null ? request.getUsername() : "null");

        if (request == null ||
                request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            log.warn("登录失败: 用户名或密码为空");
            ApiError error = new ApiError();
            error.setCode(ApiError.CodeEnum.INVALID_CREDENTIALS);
            error.setMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            log.warn("登录失败: 用户不存在 username={}", request.getUsername());
            ApiError error = new ApiError();
            error.setCode(ApiError.CodeEnum.INVALID_CREDENTIALS);
            error.setMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

//        // 详细日志记录
//        log.info("开始密码验证流程:");
//        log.info("1. 用户信息 - userId={}, username={}, isAdmin={}",
//                user.getUserId(), user.getUsername(), user.getIsAdmin());
//        log.info("2. 密码信息 - 前端传入密码长度={}, 数据库密码长度={}",
//                request.getPassword().length(), user.getPassword().length());
//        log.info("3. 数据库密码格式 - 是否以$2a$开头: {}",
//                user.getPassword().startsWith("$2a$"));
//        log.info("4. 密码验证详情:");
//        log.info("   - 前端密码: {}", request.getPassword());
//        log.info("   - 数据库密码: {}", user.getPassword());
//
//        // 添加密码加密测试
//        String testEncodedPassword = passwordEncoder.encode(request.getPassword());
//        log.info("5. 密码加密测试:");
//        log.info("   - 新加密的密码: {}", testEncodedPassword);
//        log.info("   - 新密码验证结果: {}", passwordEncoder.matches(request.getPassword(), testEncodedPassword));
//        log.info("   - 数据库密码验证结果: {}", passwordEncoder.matches(request.getPassword(), user.getPassword()));
//        log.info("6. 密码验证使用的编码器: {}", passwordEncoder.getClass().getSimpleName());
        // password123
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("登录失败: 密码错误 username={}", request.getUsername());
            ApiError error = new ApiError();
            error.setCode(ApiError.CodeEnum.INVALID_CREDENTIALS);
            error.setMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String role = user.getIsAdmin() ? "ADMIN" : "USER";
        String token = jwtUtil.generateToken(user.getUserId(), role);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRole(Role.valueOf(role));
        
        log.info("登录成功: username={}, role={}, userId={}", request.getUsername(), role, user.getUserId());
        return ResponseEntity.ok(response);
    }
}
