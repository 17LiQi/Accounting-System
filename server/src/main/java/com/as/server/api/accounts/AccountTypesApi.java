package com.as.server.api.accounts;

import com.as.server.dto.accounts.AccountTypeDTO;
import com.as.server.dto.accounts.AccountTypeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface AccountTypesApi {
    /**
     * 列出账户类型
     * @return 账户类型列表
     */
    ResponseEntity<List<AccountTypeDTO>> accountTypesList();

    /**
     * 创建账户类型
     * @param request 账户类型请求
     * @return 创建的账户类型
     */
    ResponseEntity<AccountTypeDTO> accountTypesCreate(@Valid @RequestBody AccountTypeRequest request);

    /**
     * 获取账户类型详情
     * @param typeId 账户类型ID
     * @return 账户类型详情
     */
    ResponseEntity<AccountTypeDTO> accountTypesGet(@PathVariable Integer typeId);

    /**
     * 更新账户类型
     * @param typeId 账户类型ID
     * @param request 账户类型请求
     * @return 更新后的账户类型
     */
    ResponseEntity<AccountTypeDTO> accountTypesUpdate(@PathVariable Integer typeId, @Valid @RequestBody AccountTypeRequest request);

    /**
     * 删除账户类型
     * @param typeId 账户类型ID
     * @return 无内容响应
     */
    ResponseEntity<Void> accountTypesDelete(@PathVariable Integer typeId);
} 