package com.as.server.controller;

import com.as.server.dto.statistics.StatisticsResponse;
import com.as.server.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) Integer subAccountId,
            @RequestParam String period,
            @RequestParam Integer year,
            @RequestParam(required = false) Integer month) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && (userId != null && !userId.equals(Integer.parseInt(currentUserId)))) {
            throw new AccessDeniedException("Cannot access other user's statistics");
        }
        StatisticsResponse response = statisticsService.getStatistics(
                isAdmin ? userId : Integer.parseInt(currentUserId),
                accountId,
                subAccountId,
                period,
                year,
                month);
        return ResponseEntity.ok(response);
    }
}
