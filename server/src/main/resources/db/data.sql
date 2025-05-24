-- 清除现有数据（注意：会删除所有现有记录，谨慎操作！）
DELETE FROM family_accounting.transactions;
DELETE FROM family_accounting.user_sub_accounts;
DELETE FROM family_accounting.sub_accounts;
DELETE FROM family_accounting.accounts;
DELETE FROM family_accounting.account_types;
DELETE FROM family_accounting.transaction_types;
DELETE FROM family_accounting.users;

-- 重置自增主键
ALTER TABLE family_accounting.users AUTO_INCREMENT = 1;
ALTER TABLE family_accounting.account_types AUTO_INCREMENT = 1;
ALTER TABLE family_accounting.accounts AUTO_INCREMENT = 1;
ALTER TABLE family_accounting.sub_accounts AUTO_INCREMENT = 1;
ALTER TABLE family_accounting.transaction_types AUTO_INCREMENT = 1;

-- 插入用户数据
-- 密码: password123
INSERT INTO family_accounting.users (username, password, is_admin) VALUES 
('john_doe', '$2a$10$iAAeybP6zD1J3pNze8zWUukSbw.GKnlILzpxXWfd.wJn8Rux9DSkO', 0),
('admin', '$2a$10$iAAeybP6zD1J3pNze8zWUukSbw.GKnlILzpxXWfd.wJn8Rux9DSkO', 1),
('alice', '$2a$10$iAAeybP6zD1J3pNze8zWUukSbw.GKnlILzpxXWfd.wJn8Rux9DSkO', 0),
('bob', '$2a$10$iAAeybP6zD1J3pNze8zWUukSbw.GKnlILzpxXWfd.wJn8Rux9DSkO', 0);

-- 插入账号类型
INSERT INTO family_accounting.account_types (type_name) VALUES 
('现金'),
('银行卡'),
('支付宝'),
('微信'),
('信用卡'),
('投资账户');

-- 插入账号
INSERT INTO family_accounting.accounts (type_id, account_name) VALUES 
(1, '现金账户'),
(2, '工商银行'),
(2, '建设银行'),
(2, '招商银行'),
(3, '支付宝账户'),
(4, '微信钱包'),
(5, '招商信用卡'),
(5, '工商信用卡'),
(6, '股票账户'),
(6, '基金账户');

-- 插入子账号
INSERT INTO family_accounting.sub_accounts (account_id, account_name, account_number, card_type, balance) VALUES 
(1, '现金', 'CASH001', 'WALLET', 1000.00),
(2, '工商银行储蓄卡', '6222021234567890123', 'SAVINGS', 5000.00),
(3, '建设银行储蓄卡', '6227001234567890123', 'SAVINGS', 3000.00),
(4, '招商银行储蓄卡', '6225881234567890123', 'SAVINGS', 8000.00),
(5, '支付宝余额', 'ALIPAY001', 'WALLET', 2000.00),
(6, '微信零钱', 'WECHAT001', 'WALLET', 1500.00),
(7, '招商信用卡', '6225761234567890123', 'CREDIT', 0.00),
(8, '工商信用卡', '6222021234567890124', 'CREDIT', 0.00),
(9, '股票账户', 'STOCK001', 'WALLET', 50000.00),
(10, '基金账户', 'FUND001', 'WALLET', 30000.00);

-- 插入交易类型
INSERT INTO family_accounting.transaction_types (type_name, is_income) VALUES 
('工资', 1),
('奖金', 1),
('投资收入', 1),
('兼职收入', 1),
('餐饮', 0),
('交通', 0),
('购物', 0),
('娱乐', 0),
('住房', 0),
('医疗', 0),
('教育', 0),
('旅游', 0),
('其他收入', 1),
('其他支出', 0);

-- 关联用户和子账号
INSERT INTO family_accounting.user_sub_accounts (user_id, sub_account_id) VALUES 
-- john_doe的账户
(1, 1), (1, 2), (1, 5), (1, 6), (1, 9),
-- admin的账户
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10),
-- alice的账户
(3, 1), (3, 3), (3, 5), (3, 7), (3, 10),
-- bob的账户
(4, 1), (4, 4), (4, 6), (4, 8), (4, 9);

-- 插入初始交易数据
INSERT INTO family_accounting.transactions (time, type_id, sub_account_id, amount, user_id, remarks) VALUES 
-- john_doe的交易
('2025-05-01 09:00:00', 1, 2, 10000.00, 1, '5月工资'),
('2025-05-05 14:30:00', 2, 2, 2000.00, 1, '项目奖金'),
('2025-05-10 16:00:00', 3, 9, 5000.00, 1, '股票收益'),
('2025-05-02 12:30:00', 5, 6, 50.00, 1, '午餐'),
('2025-05-03 08:00:00', 6, 6, 20.00, 1, '公交'),
('2025-05-04 15:00:00', 7, 2, 200.00, 1, '超市购物'),
('2025-05-06 19:00:00', 8, 5, 100.00, 1, '电影票'),
('2025-05-07 10:00:00', 9, 2, 3000.00, 1, '房租'),
('2025-05-08 14:00:00', 10, 2, 500.00, 1, '医院检查'),
('2025-05-09 09:00:00', 11, 2, 1000.00, 1, '培训课程'),

-- admin的交易
('2025-05-01 09:00:00', 1, 2, 15000.00, 2, '5月工资'),
('2025-05-05 14:30:00', 2, 2, 5000.00, 2, '项目奖金'),
('2025-05-10 16:00:00', 3, 9, 10000.00, 2, '股票收益'),
('2025-05-11 10:00:00', 4, 2, 3000.00, 2, '兼职收入'),
('2025-05-02 12:30:00', 5, 6, 100.00, 2, '商务午餐'),
('2025-05-03 08:00:00', 6, 6, 50.00, 2, '打车'),
('2025-05-04 15:00:00', 7, 2, 500.00, 2, '电子产品'),
('2025-05-06 19:00:00', 8, 5, 200.00, 2, '音乐会'),
('2025-05-07 10:00:00', 9, 2, 5000.00, 2, '房贷'),
('2025-05-08 14:00:00', 10, 2, 1000.00, 2, '体检'),
('2025-05-09 09:00:00', 11, 2, 2000.00, 2, 'MBA课程'),
('2025-05-12 09:00:00', 12, 2, 5000.00, 2, '出国旅游'),

-- alice的交易
('2025-05-01 09:00:00', 1, 3, 8000.00, 3, '5月工资'),
('2025-05-05 14:30:00', 2, 3, 1000.00, 3, '项目奖金'),
('2025-05-10 16:00:00', 3, 10, 3000.00, 3, '基金收益'),
('2025-05-02 12:30:00', 5, 5, 30.00, 3, '午餐'),
('2025-05-03 08:00:00', 6, 5, 15.00, 3, '公交'),
('2025-05-04 15:00:00', 7, 3, 150.00, 3, '超市购物'),
('2025-05-06 19:00:00', 8, 5, 80.00, 3, '电影票'),

-- bob的交易
('2025-05-01 09:00:00', 1, 4, 12000.00, 4, '5月工资'),
('2025-05-05 14:30:00', 2, 4, 3000.00, 4, '项目奖金'),
('2025-05-10 16:00:00', 3, 9, 8000.00, 4, '股票收益'),
('2025-05-02 12:30:00', 5, 6, 40.00, 4, '午餐'),
('2025-05-03 08:00:00', 6, 6, 25.00, 4, '公交'),
('2025-05-04 15:00:00', 7, 4, 300.00, 4, '超市购物'),
('2025-05-06 19:00:00', 8, 6, 150.00, 4, '电影票'); 