create table family_accounting.account_types
(
    type_id   int auto_increment comment '账号类型'
        primary key,
    type_name varchar(50) not null comment '账号名称',
    constraint type_name
        unique (type_name)
);

create index idx_type_name
    on family_accounting.account_types (type_name);

create table family_accounting.accounts
(
    account_id int auto_increment comment '账号id'
        primary key,
    type_id    int          not null comment '账号类型id',
    name       varchar(100) not null comment '账号名称',
    constraint accounts_ibfk_1
        foreign key (type_id) references family_accounting.account_types (type_id)
);

create index idx_name
    on family_accounting.accounts (name);

create index type_id
    on family_accounting.accounts (type_id);

create table family_accounting.sub_accounts
(
    sub_account_id int auto_increment comment '子账号id'
        primary key,
    account_id     int                                           not null comment '账号id',
    name           varchar(100)                                  not null comment '账号名称',
    account_number varchar(50)                                   not null comment '账号/卡号',
    card_type      enum ('SAVINGS', 'DEBIT', 'CREDIT', 'WALLET') not null comment '账号/卡类型',
    balance        decimal(15, 2) default 0.00                   not null comment '余额',
    constraint account_number
        unique (account_number),
    constraint sub_accounts_ibfk_1
        foreign key (account_id) references family_accounting.accounts (account_id)
            on delete cascade
);

create index idx_account_id
    on family_accounting.sub_accounts (account_id);

create index idx_name
    on family_accounting.sub_accounts (name);

create table family_accounting.transaction_types
(
    type_id   int auto_increment comment '收支id'
        primary key,
    type_name varchar(50) not null comment '收支名称',
    is_income tinyint(1)  not null comment '是否为收入TRUE 为收入，FALSE 为支出',
    constraint type_name
        unique (type_name)
);

create index idx_type_name
    on family_accounting.transaction_types (type_name);

create table family_accounting.users
(
    user_id  int auto_increment comment '用户id'
        primary key,
    username varchar(50)          not null comment '用户名称',
    password varchar(255)         not null comment '用户密码',
    is_admin tinyint(1) default 0 not null comment '是否管理员',
    constraint username
        unique (username)
);

create table family_accounting.transactions
(
    transaction_id int auto_increment comment '收支id'
        primary key,
    time           datetime       not null comment '收支时间',
    type_id        int            not null comment '收支类型',
    sub_account_id int            not null comment '子账号id',
    amount         decimal(15, 2) not null comment '金额',
    user_id        int            not null comment '用户id',
    remarks        varchar(255)   null comment '备注',
    constraint transactions_ibfk_1
        foreign key (type_id) references family_accounting.transaction_types (type_id),
    constraint transactions_ibfk_2
        foreign key (sub_account_id) references family_accounting.sub_accounts (sub_account_id),
    constraint transactions_ibfk_3
        foreign key (user_id) references family_accounting.users (user_id),
    check (`amount` >= 0)
);

create index idx_sub_account
    on family_accounting.transactions (sub_account_id);

create index idx_type
    on family_accounting.transactions (type_id);

create index idx_user_time
    on family_accounting.transactions (user_id, time);

create table family_accounting.user_sub_accounts
(
    user_id        int not null,
    sub_account_id int not null,
    primary key (user_id, sub_account_id),
    constraint user_sub_accounts_ibfk_1
        foreign key (user_id) references family_accounting.users (user_id)
            on delete cascade,
    constraint user_sub_accounts_ibfk_2
        foreign key (sub_account_id) references family_accounting.sub_accounts (sub_account_id)
            on delete cascade
);

create index sub_account_id
    on family_accounting.user_sub_accounts (sub_account_id);

create index idx_username
    on family_accounting.users (username);

