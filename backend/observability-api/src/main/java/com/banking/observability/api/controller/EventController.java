package com.banking.observability.api.controller;

import com.banking.observability.api.mapper.EventApiMapper;
import com.banking.observability.api.request.ReceiveEventRequest;
import com.banking.observability.api.response.ReceivedEventResponse;
import com.banking.observability.application.facade.ObservabilityFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {
    private final ObservabilityFacade facade;

    public EventController(ObservabilityFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReceivedEventResponse receive(@Valid @RequestBody ReceiveEventRequest request) {
        return EventApiMapper.toResponse(facade.receive(
            request.eventId(),
            request.sourceAccountId(),
            request.targetAccountId(),
            request.eventKind(),
            request.eventStatus(),
            request.amount(),
            request.currency(),
            request.eventKey(),
            request.occurredAt()
        ));
    }
}
