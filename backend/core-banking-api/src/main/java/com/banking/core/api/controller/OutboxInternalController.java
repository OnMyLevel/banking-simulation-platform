package com.banking.core.api.controller;

import com.banking.core.api.mapper.OutboxEventApiMapper;
import com.banking.core.api.response.OutboxEventResponse;
import com.banking.core.application.facade.OutboxOpsFacade;
import com.banking.core.domain.model.OutboxEventStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/outbox-events")
public class OutboxInternalController {
    private final OutboxOpsFacade facade;

    public OutboxInternalController(OutboxOpsFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public List<OutboxEventResponse> findByStatus(
        @RequestParam(defaultValue = "FAILED") OutboxEventStatus status,
        @RequestParam(defaultValue = "25") int limit,
        @RequestParam(defaultValue = "0") int offset
    ) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        int safeOffset = Math.max(0, offset);
        return facade.findByStatus(status, safeLimit, safeOffset)
            .stream()
            .map(OutboxEventApiMapper::toResponse)
            .toList();
    }

    @PostMapping("/{eventId}/retry")
    public OutboxEventResponse retryNow(@PathVariable UUID eventId) {
        return OutboxEventApiMapper.toResponse(facade.retryNow(eventId));
    }
}
