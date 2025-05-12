package com.as.server.controller;

import com.as.server.dto.transactions.TransactionTypeDTO;
import com.as.server.entity.TransactionType;
import com.as.server.exception.ConflictException;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.TransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TransactionTypeDTO> create(@RequestBody TransactionTypeDTO request) {
        TransactionType type = entityMapper.toTransactionType(request);
        TransactionType created = transactionTypeService.create(type);
        return ResponseEntity.status(201).body(entityMapper.toTransactionTypeDTO(created));
    }

    @GetMapping
    public ResponseEntity<List<TransactionTypeDTO>> list() {
        List<TransactionTypeDTO> types = transactionTypeService.findAll().stream()
                .map(entityMapper::toTransactionTypeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<TransactionTypeDTO> update(@PathVariable Integer typeId, @RequestBody TransactionTypeDTO request) {
        TransactionType type = entityMapper.toTransactionType(request);
        TransactionType updated = transactionTypeService.update(typeId, type);
        return ResponseEntity.ok(entityMapper.toTransactionTypeDTO(updated));
    }

    @DeleteMapping("/{typeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer typeId) {
        if (transactionTypeService.hasTransactions(typeId)) {
            throw new ConflictException("Transaction type has associated transactions");
        }
        transactionTypeService.delete(typeId);
        return ResponseEntity.noContent().build();
    }
}
