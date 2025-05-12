package com.as.server.mapper.impl;

import com.as.server.dto.accounts.AccountDTO;
import com.as.server.dto.accounts.AccountRequest;
import com.as.server.dto.accounts.SubAccountDTO;
import com.as.server.dto.accounts.SubAccountRequest;
import com.as.server.dto.transactions.TransactionDTO;
import com.as.server.dto.transactions.TransactionRequest;
import com.as.server.dto.transactions.TransactionTypeDTO;
import com.as.server.dto.users.UserDTO;
import com.as.server.dto.users.UserRequest;
import com.as.server.entity.*;
import com.as.server.enums.CardType;
import com.as.server.mapper.EntityMapper;
import com.as.server.repository.AccountTypeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2025-05-11T11:33:10+0800",
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

        userDTO.setRole( user.getIsAdmin() ? com.as.server.dto.users.UserDTO.RoleEnum.ADMIN : com.as.server.dto.users.UserDTO.RoleEnum.USER );

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
        accountDTO.setName( account.getName() );
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

        account.setName( request.getName() );
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
        subAccountDTO.setName( subAccount.getName() );
        subAccountDTO.setAccountNumber( subAccount.getAccountNumber() );
        subAccountDTO.setCardType( cardTypeToCardTypeEnum( subAccount.getCardType() ) );
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
        subAccount.setName( request.getName() );
        subAccount.setAccountNumber( request.getAccountNumber() );
        subAccount.setCardType( cardTypeEnumToCardType( request.getCardType() ) );
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
        transactionTypeDTO.settypeName( type.getTypeName() );
        transactionTypeDTO.setIsIncome( type.getIsIncome() );

        return transactionTypeDTO;
    }

    @Override
    public TransactionType toTransactionType(TransactionTypeDTO request) {
        if ( request == null ) {
            return null;
        }

        TransactionType transactionType = new TransactionType();

        transactionType.setTypeId( request.getTypeId() );
        transactionType.setTypeName( request.gettypeName() );
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

    protected SubAccountDTO.CardTypeEnum cardTypeToCardTypeEnum(CardType cardType) {
        if ( cardType == null ) {
            return null;
        }

        SubAccountDTO.CardTypeEnum cardTypeEnum;

        switch ( cardType ) {
            case SAVINGS: cardTypeEnum = SubAccountDTO.CardTypeEnum.SAVINGS;
                break;
            case DEBIT: cardTypeEnum = SubAccountDTO.CardTypeEnum.DEBIT;
                break;
            case CREDIT: cardTypeEnum = SubAccountDTO.CardTypeEnum.CREDIT;
                break;
            case WALLET: cardTypeEnum = SubAccountDTO.CardTypeEnum.WALLET;
                break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + cardType );
        }

        return cardTypeEnum;
    }

    protected CardType cardTypeEnumToCardType(SubAccountRequest.CardTypeEnum cardTypeEnum) {
        if ( cardTypeEnum == null ) {
            return null;
        }

        CardType cardType;

        switch ( cardTypeEnum ) {
            case SAVINGS: cardType = CardType.SAVINGS;
                break;
            case DEBIT: cardType = CardType.DEBIT;
                break;
            case CREDIT: cardType = CardType.CREDIT;
                break;
            case WALLET: cardType = CardType.WALLET;
                break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + cardTypeEnum );
        }

        return cardType;
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