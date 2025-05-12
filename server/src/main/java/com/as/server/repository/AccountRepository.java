package com.as.server.repository;

import com.as.server.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT COUNT(s) > 0 FROM SubAccount s WHERE s.account.accountId = :accountId")
    boolean existsByIdWithSubAccounts(Integer accountId);
}
