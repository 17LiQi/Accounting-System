package com.as.server.entity;

import com.as.server.enums.CardType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter

@Slf4j
@Table(name = "sub_accounts")
public class SubAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_account_id")
    private Integer subAccountId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ManyToMany
    @JoinTable(
            name = "user_sub_accounts",
            joinColumns = @JoinColumn(name = "sub_account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }

    @Override
    public String toString() {
        return "SubAccount{" +
                "subAccountId=" + subAccountId +
                ", accountName='" + accountName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", cardType=" + cardType +
                ", balance=" + balance +
                '}';
    }

}
