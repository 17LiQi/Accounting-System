package com.as.server.service;

import com.as.server.entity.SubAccount;

import java.util.List;

public interface SubAccountService {
    /**
     * 创建子账号
     * @param subAccount 子账号信息
     * @return 创建后的子账号
     */
    SubAccount create(SubAccount subAccount);

    /**
     * 更新子账号
     * @param id 子账号ID
     * @param subAccount 更新的子账号信息
     * @return 更新后的子账号
     */
    SubAccount update(Integer id, SubAccount subAccount);

    /**
     * 根据ID查找子账号
     * @param id 子账号ID
     * @return 子账号信息
     */
    SubAccount findById(Integer id);

    /**
     * 查找所有子账号
     * @return 子账号列表
     */
    List<SubAccount> findAll();

    /**
     * 删除子账号
     * @param id 子账号ID
     */
    void delete(Integer id);

    /**
     * 检查子账号是否有关联交易
     * @param id 子账号ID
     * @return 是否有关联交易
     */
    boolean hasTransactions(Integer id);
}
