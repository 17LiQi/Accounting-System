# 家庭记账系统数据库对比与改进思路

## 1. 原始需求
### 1.1 数据库结构（推测）
- **sub_accounts**：子账户信息（编号、余额等）。
- **transactions**：交易记录（时间、金额、类型等）。
- **transaction_types**：交易类型（收入/支出）。
- **users**：用户信息（管理员/普通用户）。
- **user_sub_accounts**：用户-子账户关联。
- **accounts**：顶级账户（银行、微信等）。
- **特点**：
    - 金额为字符串（如 `"12345.67"`）。
    - 未明确字段类型、外键、索引。
    - 权限：管理员全访问，普通用户限关联子账户。

### 1.2 功能需求
- 子账户管理：创建/更新/删除，检查唯一性。
- 交易管理：创建交易，查询列表。
- 统计：按月/年统计收支，按类型分组。

## 2. 最终实现
### 2.1 数据库结构
1. **sub_accounts**：
    - `sub_account_id` (INT, PK, AUTO_INCREMENT)
    - `account_id` (INT, FK → accounts)
    - `name` (VARCHAR(100))
    - `account_number` (VARCHAR(50), UNIQUE)
    - `card_type` (VARCHAR(20), ENUM: SAVINGS, DEBIT, CREDIT, WALLET)
    - `balance` (DECIMAL(15,2))
2. **transactions**：
    - `transaction_id` (INT, PK, AUTO_INCREMENT)
    - `time` (DATETIME)
    - `type_id` (INT, FK → transaction_types)
    - `is_income` (BOOLEAN)
    - `sub_account_id` (INT, FK → sub_accounts)
    - `amount` (DECIMAL(15,2))
    - `user_id` (INT, FK → users)
    - `remarks` (VARCHAR(255))
3. **transaction_types**：
    - `type_id` (INT, PK, AUTO_INCREMENT)
    - `name` (VARCHAR(50))
    - `is_income` (BOOLEAN)
4. **users**：
    - `user_id` (INT, PK, AUTO_INCREMENT)
    - `username` (VARCHAR(50), UNIQUE)
    - `password` (VARCHAR(255))
    - `role` (VARCHAR(20), ENUM: ADMIN, USER)
5. **user_sub_accounts**：
    - `user_id` (INT, FK → users)
    - `sub_account_id` (INT, FK → sub_accounts)
    - 复合主键：(user_id, sub_account_id)
6. **accounts**：
    - `account_id` (INT, PK, AUTO_INCREMENT)
    - `name` (VARCHAR(50))

### 2.2 特点
- **JPA 映射**：使用 `@Entity` 映射表。
- **数据类型**：`DECIMAL(15,2)` 用于金额，`VARCHAR` 模拟 ENUM。
- **外键**：确保数据一致性。
- **索引**：`sub_accounts.account_number` (UNIQUE)。

## 3. 对比
| 方面           | 原始需求                              | 最终实现                              | 改进说明                              |
|----------------|--------------------------------------|--------------------------------------|--------------------------------------|
| 表结构         | 推测 6 张表，字段未明确              | 明确 6 张表，定义字段、外键          | 提升数据完整性                        |
| 金额处理       | 字符串（如 "12345.67"）              | 数据库 DECIMAL，API 字符串            | 数据库精度高，API 兼容前端             |
| 权限控制       | 管理员全访问，用户限关联子账户        | Spring Security + JWT                | 细化权限控制                          |
| 索引           | 未明确                               | UNIQUE 索引，建议交易索引             | 提高查询性能                          |
| 数据验证       | OpenAPI required, pattern            | JPA + @Valid                         | 双层验证                              |
| 扩展性         | 未考虑多币种、审计日志               | 基本功能，简洁设计                   | 可扩展多币种、审计日志                |
