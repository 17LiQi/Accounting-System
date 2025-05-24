package com.as.server.mapper;

import com.as.server.dto.accounts.AccountDTO;
import com.as.server.dto.accounts.AccountRequest;
import com.as.server.dto.accounts.AccountTypeDTO;
import com.as.server.dto.accounts.AccountTypeRequest;
import com.as.server.dto.accounts.SubAccountDTO;
import com.as.server.dto.accounts.SubAccountRequest;
import com.as.server.dto.transactions.*;
import com.as.server.dto.users.UserDTO;
import com.as.server.dto.users.UserRequest;
import com.as.server.entity.*;
import com.as.server.enums.AccountType;
import com.as.server.repository.AccountTypeRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.stream.Collectors;

// @Mapper
public interface EntityMapper {

    Logger logger = LoggerFactory.getLogger(EntityMapper.class);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "role", expression = "java(user.getIsAdmin() ? com.as.server.enums.Role.ADMIN : com.as.server.enums.Role.USER)")
    UserDTO toUserDTO(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "isAdmin", expression = "java(\"ADMIN\".equals(request.getRole()))")
    @Mapping(target = "transactions", ignore = true)
    User toUser(UserRequest request);

    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "accountName", source = "accountName")
    @Mapping(target = "typeId", source = "accountType.typeId")
    @Mapping(target = "type", source = "accountType.typeName", qualifiedByName = "stringToTypeEnum")
    AccountDTO toAccountDTO(Account account);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "accountName", source = "accountName")
    @Mapping(source = "typeId", target = "accountType", qualifiedByName = "accountType")
    @Mapping(target = "subAccounts", ignore = true)
    Account toAccount(AccountRequest request, @Context AccountTypeRepository accountTypeRepository);

    @Mapping(target = "subAccountId", source = "subAccountId")
    @Mapping(target = "accountId", source = "account.accountId")
    @Mapping(target = "accountName", source = "accountName")
    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "cardType", source = "cardType")
    @Mapping(target = "balance", source = "balance", qualifiedByName = "bigDecimalToString")
    SubAccountDTO toSubAccountDTO(SubAccount subAccount);

    @Mapping(target = "subAccountId", ignore = true)
    @Mapping(target = "account", source = "accountId", qualifiedByName = "toAccount")
    @Mapping(target = "accountName", source = "accountName")
    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "cardType", source = "cardType")
    @Mapping(target = "balance", source = "balance", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "users", ignore = true)
    SubAccount toSubAccount(SubAccountRequest request);

    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "time", source = "time", qualifiedByName = "localDateTimeToOffsetDateTime")
    @Mapping(target = "typeId", source = "transactionType.typeId")
    @Mapping(target = "isIncome", source = "transactionType.isIncome")
    @Mapping(target = "subAccountId", source = "subAccount.subAccountId")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "bigDecimalToString")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "remarks", source = "remarks")
    TransactionDTO toTransactionDTO(Transaction transaction);

    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "time", source = "time", qualifiedByName = "offsetDateTimeToLocalDateTime")
    @Mapping(target = "transactionType", source = "typeId", qualifiedByName = "toTransactionType")
    @Mapping(target = "subAccount", source = "subAccountId", qualifiedByName = "toSubAccount")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "remarks", source = "remarks")
    Transaction toTransaction(TransactionRequest request);

    @Mapping(target = "typeId", source = "typeId")
    @Mapping(target = "typeName", source = "typeName")
    @Mapping(target = "isIncome", source = "isIncome")
    TransactionTypeDTO toTransactionTypeDTO(TransactionType type);

    @Mapping(target = "typeId", ignore = true)
    @Mapping(target = "typeName", source = "typeName")
    @Mapping(target = "isIncome", source = "isIncome")
    @Mapping(target = "transactions", ignore = true)
    TransactionType toTransactionType(TransactionTypeRequest request);

    default TransactionListResponse toTransactionListResponse(Page<Transaction> transactions) {
        if (transactions == null) {
            logger.warn("Transactions page is null");
            return null;
        }
        TransactionListResponse response = new TransactionListResponse();
        response.setTransactions(transactions.getContent().stream()
                .map(this::toTransactionDTO)
                .collect(Collectors.toList()));
        response.setTotal((int) transactions.getTotalElements());
        logger.debug("Mapped {} transactions to TransactionListResponse with total={}",
                transactions.getContent().size(), transactions.getTotalElements());
        return response;
    }

    // 工具方法
    @Named("bigDecimalToString")
    default String bigDecimalToString(BigDecimal value) {
        return value != null ? value.setScale(2, RoundingMode.HALF_UP).toString() : null;
    }

    @Named("stringToBigDecimal")
    default BigDecimal stringToBigDecimal(String value) {
        return value != null ? new BigDecimal(value) : null;
    }

    @Named("stringToTypeEnum")
    default AccountType stringToTypeEnum(String typeName) {
        return typeName != null ? AccountType.fromValue(typeName) : null;
    }

    @Named("localDateTimeToOffsetDateTime")
    default OffsetDateTime localDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(ZoneOffset.ofHours(8)) : null;
    }

    @Named("offsetDateTimeToLocalDateTime")
    default LocalDateTime offsetDateTimeToLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
    }

    @Named("accountType")
    default com.as.server.entity.AccountType accountType(Integer typeId, @Context AccountTypeRepository accountTypeRepository) {
        if (typeId == null) {
            logger.warn("Account type ID is null");
            return null;
        }
        Optional<com.as.server.entity.AccountType> accountTypeOptional = accountTypeRepository.findById(typeId);
        if (accountTypeOptional.isPresent()) {
            logger.debug("Found AccountType for typeId {}: {}", typeId, accountTypeOptional.get());
        } else {
            logger.debug("No AccountType found for typeId {}", typeId);
        }
        return accountTypeOptional.orElseThrow(() -> new IllegalArgumentException("AccountType with id " + typeId + " not found"));
    }

    @Named("toAccount")
    default Account toAccount(Integer accountId) {
        if (accountId == null) {
            return null;
        }
        Account account = new Account();
        account.setAccountId(accountId);
        return account;
    }

    @Named("toTransactionType")
    default TransactionType toTransactionType(Integer typeId) {
        if (typeId == null) {
            return null;
        }
        TransactionType transactionType = new TransactionType();
        transactionType.setTypeId(typeId);
        return transactionType;
    }

    @Named("toSubAccount")
    default SubAccount toSubAccount(Integer subAccountId) {
        if (subAccountId == null) {
            return null;
        }
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(subAccountId);
        return subAccount;
    }

    AccountTypeDTO toAccountTypeDTO(com.as.server.entity.AccountType accountType);
    com.as.server.entity.AccountType toAccountType(AccountTypeRequest request);
}