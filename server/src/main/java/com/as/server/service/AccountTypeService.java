package com.as.server.service;

import com.as.server.entity.AccountType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CRUD
 */
public interface AccountTypeService {
    AccountType create (AccountType accountType);
    AccountType findById (Integer id);
    List<AccountType> findAll ();
    AccountType update (Integer  id, AccountType accountType);
    void delete (Integer id);

    @Transactional(readOnly = true)
    boolean hasTransactions(Integer typeId);
}
