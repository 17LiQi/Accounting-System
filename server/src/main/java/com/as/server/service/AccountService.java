package com.as.server.service;

import com.as.server.entity.Account;

import java.util.List;

public interface AccountService {
    Account create(Account account);
    Account findById(Integer id);
    List<Account> findAll();
    Account update(Integer id, Account account);
    void delete(Integer id);

    boolean hasSubAccounts(Integer accountId);
}