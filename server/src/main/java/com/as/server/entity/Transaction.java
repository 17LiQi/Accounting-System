package com.as.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "transactions")
@Getter
@Setter
@Entity

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private TransactionType transactionType;

    @ManyToOne
        @JoinColumn(name = "sub_account_id", nullable = false)
    private SubAccount subAccount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

        @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "remarks")
    private String remarks;

}
