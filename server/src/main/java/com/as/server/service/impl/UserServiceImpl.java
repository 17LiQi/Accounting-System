package com.as.server.service.impl;

import com.as.server.entity.User;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.SubAccountRepository;
import com.as.server.repository.TransactionRepository;
import com.as.server.repository.UserRepository;
import com.as.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SubAccountRepository subAccountRepository;

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public User create(User user) {
        log.info("Creating user: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> findAll() {
        log.info("Finding all users");
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(Integer id, User user) {
        log.info("Updating user with id: {}", id);
        User existing = findById(id);
        existing.setUsername(user.getUsername());
        existing.setPassword(user.getPassword());
        existing.setIsAdmin(user.getIsAdmin());
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAssociatedData(Integer userId) {
        return !subAccountRepository.findSubAccountsByUserId(userId).isEmpty() ||
                transactionRepository.existsByUserUserId(userId);
    }
}
