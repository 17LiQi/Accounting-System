package com.as.server.controller;

import com.as.server.dto.transactions.TransactionTypeDTO;
import com.as.server.dto.transactions.TransactionTypeRequest;
import com.as.server.entity.TransactionType;
import com.as.server.exception.ConflictException;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.TransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction-types")
@PreAuthorize("hasRole('ADMIN')")
public class TransactionTypeController {

    @Autowired
    private TransactionTypeService transactionTypeService;

    @Autowired
    private EntityMapper entityMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionTypeDTO> create(@Valid @RequestBody TransactionTypeRequest request) {
        TransactionType transactionType = entityMapper.toTransactionType(request);
        TransactionType created = transactionTypeService.create(transactionType);
        return ResponseEntity.status(201).body(entityMapper.toTransactionTypeDTO(created));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<TransactionTypeDTO>> list() {
        List<TransactionType> types = transactionTypeService.findAll();
        List<TransactionTypeDTO> typeDTOs = types.stream()
                .map(entityMapper::toTransactionTypeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(typeDTOs);
    }

    @PutMapping("/{typeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionTypeDTO> update(@PathVariable Integer typeId, @Valid @RequestBody TransactionTypeRequest request) {
        TransactionType transactionType = entityMapper.toTransactionType(request);
        TransactionType updated = transactionTypeService.update(typeId, transactionType);
        return ResponseEntity.ok(entityMapper.toTransactionTypeDTO(updated));
    }

    @DeleteMapping("/{typeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer typeId) {
        if (transactionTypeService.hasTransactions(typeId)) {
            throw new ConflictException("Transaction type has associated transactions");
        }
        transactionTypeService.delete(typeId);
        return ResponseEntity.noContent().build();
    }
}
