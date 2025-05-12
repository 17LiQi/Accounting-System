package com.as.server.ServiceImplTest;

import com.as.server.entity.AccountType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.AccountTypeRepository;
import com.as.server.service.impl.AccountTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 单元测试
 * ExtendWith注解类似旧版本中的@RunWith
 */
@ExtendWith(MockitoExtension.class)
public class AccountTypeServiceImplTest {
    /**
     * 模拟对象Mock Object，用于替代真实依赖项
     */
    @Mock
    private AccountTypeRepository accountTypeRepository;

    /**
     * 真实的被测类实,自动将 @Mock 注解的对象注入到其中
     */
    @InjectMocks
    private AccountTypeServiceImpl accountTypeService;

    private AccountType accountType;

    /**
     * 初始化测试数据,在每个测试方法执行前自动调用一次,避免相互干扰
     */
    @BeforeEach
    void setUp() {
        accountType = new AccountType();
        accountType.setTypeId(1);
        accountType.setTypeName("Savings");
    }

    /**
     * shouldSaveAccountType
     */
    @Test
    void createAccountType() {
        when(accountTypeRepository.save(any(AccountType.class))).thenReturn(accountType);

        AccountType result = accountTypeService.create(accountType);

        //  断言结果
        assertNotNull(result); // 断言结果不为空
        assertEquals("Savings", result.getTypeName()); // 断言结果与预期结果一致

        verify(accountTypeRepository).save(accountType); // 验证save方法被调用
    }

    /**
     * shouldReturnAccountType
     */
    @Test
    void findAccountById() {
        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));

        AccountType result = accountTypeService.findById(1);

        assertNotNull(result);
        assertEquals("Savings", result.getTypeName());

        verify(accountTypeRepository).findById(1);
    }

    /**
     * shouldThrowEntityNotFoundException
     */
    @Test
    void findAccountById_ThrowEntityNotFoundException() {
        when(accountTypeRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountTypeService.findById(1));

        assertEquals("Account type not found with id: 1", exception.getMessage());
        verify(accountTypeRepository).findById(1);
    }

    /**
     * shouldReturnAccountTypeList
     */
    @Test
    void findAllAccount() {
        when(accountTypeRepository.findAll()).thenReturn(Collections.singletonList(accountType));

        List<AccountType> result = accountTypeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size()); // 只有一个元素
        assertEquals("Savings", result.get(0).getTypeName());

        verify(accountTypeRepository).findAll();
    }

    /**
     * shouldUpdateAccountType
     */
    @Test
    void updateAccount() {
        AccountType updated = new AccountType();
        updated.setTypeName("Checking");

        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));
        when(accountTypeRepository.save(any(AccountType.class))).thenReturn(accountType);

        AccountType result = accountTypeService.update(1, updated);

        assertNotNull(result);
        assertEquals("Checking", result.getTypeName());
        verify(accountTypeRepository).findById(1);
        verify(accountTypeRepository).save(accountType);

    }

    @Test
    public void DeleteAccount() {
        when(accountTypeRepository.existsById(1)).thenReturn(true);
        accountTypeService.delete(1);
        verify(accountTypeRepository).deleteById(1);
        verify(accountTypeRepository).existsById(1);
    }

    @Test
    public void DeleteAccount_ThrowEntityNotFoundException() {
        when(accountTypeRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException  exception = assertThrows(EntityNotFoundException.class,
                () -> accountTypeService.delete(1));

        assertEquals("Account type not found with id : 1", exception.getMessage());

        verify(accountTypeRepository).existsById(1);
        verify(accountTypeRepository).existsById(1);
    }
}
