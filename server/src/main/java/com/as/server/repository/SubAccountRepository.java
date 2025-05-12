package com.as.server.repository;

import com.as.server.entity.SubAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubAccountRepository extends JpaRepository<SubAccount, Integer> {
    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT s FROM SubAccount s JOIN s.users u WHERE u.userId = :userId")
    List<SubAccount> findSubAccountsByUserId(@Param("userId") Integer userId);

}
