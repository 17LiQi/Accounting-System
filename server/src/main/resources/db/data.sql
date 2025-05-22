-- 插入用户数据
-- 密码: password123
INSERT INTO family_accounting.users (username, password, is_admin) VALUES 
('john_doe', '$2a$10$iAAeybP6zD1J3pNze8zWUukSbw.GKnlILzpxXWfd.wJn8Rux9DSkO', 0),
('admin', '$2a$10$iAAeybP6zD1J3pNze8zWUukSbw.GKnlILzpxXWfd.wJn8Rux9DSkO', 1);

-- 插入账号类型
INSERT INTO family_accounting.account_types (type_name) VALUES 
('现金'),
('银行卡'),
('支付宝'),
('微信');

-- 插入账号
INSERT INTO family_accounting.accounts (type_id, account_name) VALUES 
(1, '现金账户'),
(2, '工商银行'),
(3, '支付宝账户'),
(4, '微信钱包');

-- 插入子账号
INSERT INTO family_accounting.sub_accounts (account_id, account_name, account_number, card_type, balance) VALUES 
(1, '现金', 'CASH001', 'WALLET', 1000.00),
(2, '工商银行储蓄卡', '6222021234567890123', 'SAVINGS', 5000.00),
(3, '支付宝余额', 'ALIPAY001', 'WALLET', 2000.00),
(4, '微信零钱', 'WECHAT001', 'WALLET', 1500.00);

-- 插入交易类型
INSERT INTO family_accounting.transaction_types (type_name, is_income) VALUES 
('工资', 1),
('奖金', 1),
('投资收入', 1),
('餐饮', 0),
('交通', 0),
('购物', 0),
('娱乐', 0);

-- 关联用户和子账号
INSERT INTO family_accounting.user_sub_accounts (user_id, sub_account_id) VALUES 
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(2, 2),
(2, 3),
(2, 4);

-- 插入初始交易数据
INSERT INTO family_accounting.transactions (time, type_id, sub_account_id, amount, user_id, remarks) VALUES 
-- 收入交易
('2025-05-01 09:00:00', 1, 2, 10000.00, 1, '5月工资'),
('2025-05-05 14:30:00', 2, 2, 2000.00, 1, '项目奖金'),
('2025-05-10 16:00:00', 3, 3, 5000.00, 1, '股票收益'),

-- 支出交易
('2025-05-02 12:30:00', 4, 4, 50.00, 1, '午餐'),
('2025-05-03 08:00:00', 5, 4, 20.00, 1, '公交'),
('2025-05-04 15:00:00', 6, 2, 200.00, 1, '超市购物'),
('2025-05-06 19:00:00', 7, 3, 100.00, 1, '电影票'),

-- 管理员交易
('2025-05-01 09:00:00', 1, 2, 15000.00, 2, '5月工资'),
('2025-05-05 14:30:00', 2, 2, 5000.00, 2, '项目奖金'),
('2025-05-10 16:00:00', 3, 3, 10000.00, 2, '股票收益'); 