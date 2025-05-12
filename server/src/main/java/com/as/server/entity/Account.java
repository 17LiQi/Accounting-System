package com.as.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AccountType accountType;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @OneToMany(mappedBy = "account")
    private List<SubAccount> subAccounts = new ArrayList<>();


}
