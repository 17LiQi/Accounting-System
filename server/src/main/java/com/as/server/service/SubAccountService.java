package com.as.server.service;

import com.as.server.entity.SubAccount;

import java.util.List;

public interface SubAccountService {
    SubAccount create(SubAccount subAccount);
    SubAccount findById(Integer id);
    List<SubAccount> findAll();
    SubAccount update(Integer id, SubAccount subAccount);
    void delete(Integer id);

    boolean hasTransactions(Integer subAccountId);
}
