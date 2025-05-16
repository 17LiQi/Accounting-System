package com.as.server.service;

import com.as.server.entity.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User findById(Integer id);
    List<User> findAll();
    User update(Integer id, User user);
    void delete(Integer id);

    User findByUsername(String username);

    boolean hasTransactions(Integer userId);
}
