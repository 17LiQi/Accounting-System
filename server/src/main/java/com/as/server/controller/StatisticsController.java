package com.as.server.controller;

import com.as.server.dto.statistics.StatisticsResponse;
import com.as.server.enums.Period;
import com.as.server.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StatisticsResponse> getStatistics(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) Integer subAccountId,
            @RequestParam String period,
            @RequestParam Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer week,
            @RequestParam(required = false) Integer day) {
        log.debug("Received request: period={}, year={}, month={}, week={}, day={}", period, year, month, week, day);

        Period periodEnum;
        try {
            periodEnum = Period.fromValue(period);
        } catch (IllegalArgumentException e) {
            log.error("Failed to parse period: {}", period, e);
            throw new IllegalArgumentException("Invalid period value: " + period, e);
        }

        if (periodEnum == null || year == null) {
            throw new IllegalArgumentException("Period and year are required");
        }
        if (periodEnum == Period.MONTHLY && month == null) {
            throw new IllegalArgumentException("Month is required for monthly period");
        }
        if (periodEnum == Period.WEEKLY && week == null) {
            throw new IllegalArgumentException("Week is required for weekly period");
        }
        if (periodEnum == Period.DAILY && (month == null || day == null)) {
            throw new IllegalArgumentException("Month and day are required for daily period");
        }

        Authentication auth = getContext().getAuthentication();
        Integer effectiveUserId = userId;
        if (userId == null) {
            effectiveUserId = Integer.valueOf(auth.getName());
            log.debug("No userId provided, using authenticated userId: {}", effectiveUserId);
        } else if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !userId.equals(Integer.valueOf(auth.getName()))) {
            log.warn("User {} attempted to access statistics of user {}", auth.getName(), userId);
            throw new AccessDeniedException("Cannot access other user's statistics");
        }

        StatisticsResponse response = statisticsService.getStatistics(
                effectiveUserId, accountId, subAccountId, periodEnum, year, month, week, day);
        log.debug("Returning response: period={}, year={}, totalIncome={}",
                response.getPeriod(), response.getYear(), response.getTotalIncome());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}