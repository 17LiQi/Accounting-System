package com.as.server.mapper.impl;

import com.as.server.dto.accounts.AccountDTO;
import com.as.server.dto.accounts.AccountRequest;
import com.as.server.dto.accounts.SubAccountDTO;
import com.as.server.dto.accounts.SubAccountRequest;
import com.as.server.dto.transactions.TransactionDTO;
import com.as.server.dto.transactions.TransactionRequest;
import com.as.server.dto.transactions.TransactionTypeDTO;
import com.as.server.dto.transactions.TransactionTypeRequest;
import com.as.server.dto.users.UserDTO;
import com.as.server.dto.users.UserRequest;
import com.as.server.entity.*;
import com.as.server.mapper.EntityMapper;
import com.as.server.repository.AccountTypeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2025-05-13T20:57:53+0800",
        comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_421 (Oracle Corporation)"
)
@Component
public class EntityMapperImpl implements EntityMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId( user.getUserId() );
        userDTO.setUsername( user.getUsername() );

        userDTO.setRole( user.getIsAdmin() ? com.as.server.enums.Role.ADMIN : com.as.server.enums.Role.USER );

        return userDTO;
    }

    @Override
    public User toUser(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( request.getPassword() );
        user.setUsername( request.getUsername() );

        user.setIsAdmin( "ADMIN".equals(request.getRole()) );

        return user;
    }

    @Override
    public AccountDTO toAccountDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setAccountId( account.getAccountId() );
        accountDTO.setAccountName( account.getAccountName() );
        accountDTO.setTypeId( accountAccountTypeTypeId( account ) );
        accountDTO.setType( stringToTypeEnum( accountAccountTypeTypeName( account ) ) );

        return accountDTO;
    }

    @Override
    public Account toAccount(AccountRequest request, AccountTypeRepository accountTypeRepository) {
        if ( request == null ) {
            return null;
        }

        Account account = new Account();

        account.setAccountName( request.getaccountName() );
        account.setAccountType( accountType( request.getTypeId(), accountTypeRepository ) );

        return account;
    }

    @Override
    public SubAccountDTO toSubAccountDTO(SubAccount subAccount) {
        if ( subAccount == null ) {
            return null;
        }

        SubAccountDTO subAccountDTO = new SubAccountDTO();

        subAccountDTO.setSubAccountId( subAccount.getSubAccountId() );
        subAccountDTO.setAccountId( subAccountAccountAccountId( subAccount ) );
        subAccountDTO.setaccountName( subAccount.getAccountName() );
        subAccountDTO.setAccountNumber( subAccount.getAccountNumber() );
        subAccountDTO.setCardType( subAccount.getCardType() );
        subAccountDTO.setBalance( bigDecimalToString( subAccount.getBalance() ) );

        return subAccountDTO;
    }

    @Override
    public SubAccount toSubAccount(SubAccountRequest request) {
        if ( request == null ) {
            return null;
        }

        SubAccount subAccount = new SubAccount();

        subAccount.setAccount( toAccount( request.getAccountId() ) );
        subAccount.setAccountName( request.getaccountName() );
        subAccount.setAccountNumber( request.getAccountNumber() );
        subAccount.setCardType( request.getCardType() );
        subAccount.setBalance( stringToBigDecimal( request.getBalance() ) );

        return subAccount;
    }

    @Override
    public TransactionDTO toTransactionDTO(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setTransactionId( transaction.getTransactionId() );
        transactionDTO.setTime( localDateTimeToOffsetDateTime( transaction.getTime() ) );
        transactionDTO.setTypeId( transactionTransactionTypeTypeId( transaction ) );
        transactionDTO.setIsIncome( transactionTransactionTypeIsIncome( transaction ) );
        transactionDTO.setSubAccountId( transactionSubAccountSubAccountId( transaction ) );
        transactionDTO.setAmount( bigDecimalToString( transaction.getAmount() ) );
        transactionDTO.setUserId( transactionUserUserId( transaction ) );
        transactionDTO.setRemarks( transaction.getRemarks() );

        return transactionDTO;
    }

    @Override
    public Transaction toTransaction(TransactionRequest request) {
        if ( request == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setTime( offsetDateTimeToLocalDateTime( request.getTime() ) );
        transaction.setTransactionType( toTransactionType( request.getTypeId() ) );
        transaction.setSubAccount( toSubAccount( request.getSubAccountId() ) );
        transaction.setAmount( stringToBigDecimal( request.getAmount() ) );
        transaction.setRemarks( request.getRemarks() );

        return transaction;
    }

    @Override
    public TransactionTypeDTO toTransactionTypeDTO(TransactionType type) {
        if ( type == null ) {
            return null;
        }

        TransactionTypeDTO transactionTypeDTO = new TransactionTypeDTO();

        transactionTypeDTO.setTypeId( type.getTypeId() );
        transactionTypeDTO.setTypeName( type.getTypeName() );
        transactionTypeDTO.setIsIncome( type.getIsIncome() );

        return transactionTypeDTO;
    }

    @Override
    public TransactionType toTransactionType(TransactionTypeRequest request) {
        if ( request == null ) {
            return null;
        }

        TransactionType transactionType = new TransactionType();

        transactionType.setTypeName( request.getTypeName() );
        transactionType.setIsIncome( request.getIsIncome() );

        return transactionType;
    }

    private Integer accountAccountTypeTypeId(Account account) {
        if ( account == null ) {
            return null;
        }
        AccountType accountType = account.getAccountType();
        if ( accountType == null ) {
            return null;
        }
        Integer typeId = accountType.getTypeId();
        if ( typeId == null ) {
            return null;
        }
        return typeId;
    }

    private String accountAccountTypeTypeName(Account account) {
        if ( account == null ) {
            return null;
        }
        AccountType accountType = account.getAccountType();
        if ( accountType == null ) {
            return null;
        }
        String typeName = accountType.getTypeName();
        if ( typeName == null ) {
            return null;
        }
        return typeName;
    }

    private Integer subAccountAccountAccountId(SubAccount subAccount) {
        if ( subAccount == null ) {
            return null;
        }
        Account account = subAccount.getAccount();
        if ( account == null ) {
            return null;
        }
        Integer accountId = account.getAccountId();
        if ( accountId == null ) {
            return null;
        }
        return accountId;
    }

    private Integer transactionTransactionTypeTypeId(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        TransactionType transactionType = transaction.getTransactionType();
        if ( transactionType == null ) {
            return null;
        }
        Integer typeId = transactionType.getTypeId();
        if ( typeId == null ) {
            return null;
        }
        return typeId;
    }

    private Boolean transactionTransactionTypeIsIncome(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        TransactionType transactionType = transaction.getTransactionType();
        if ( transactionType == null ) {
            return null;
        }
        Boolean isIncome = transactionType.getIsIncome();
        if ( isIncome == null ) {
            return null;
        }
        return isIncome;
    }

    private Integer transactionSubAccountSubAccountId(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        SubAccount subAccount = transaction.getSubAccount();
        if ( subAccount == null ) {
            return null;
        }
        Integer subAccountId = subAccount.getSubAccountId();
        if ( subAccountId == null ) {
            return null;
        }
        return subAccountId;
    }

    private Integer transactionUserUserId(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        User user = transaction.getUser();
        if ( user == null ) {
            return null;
        }
        Integer userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
