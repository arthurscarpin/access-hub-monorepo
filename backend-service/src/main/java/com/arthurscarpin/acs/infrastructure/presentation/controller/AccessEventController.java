package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.usecase.GetAccessHistoryUseCase;
import com.arthurscarpin.acs.core.accessevent.usecase.ValidateAccessUseCase;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanReadAccessEvent;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanWriteAccessEvent;
import com.arthurscarpin.acs.infrastructure.mapper.AccessEventMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.AccessEventControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.AccessEventRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.AccessEventResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/access-events")
@RequiredArgsConstructor
public class AccessEventController implements AccessEventControllerDoc {

    private final ValidateAccessUseCase validateAccessUseCase;

    private final GetAccessHistoryUseCase getAccessHistoryUseCase;

    private final AccessEventMapper mapper;

    @CanWriteAccessEvent
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccessEventResponse save(@Valid @RequestBody AccessEventRequest request) {
        log.info("Starting access validation for plate: {}, direction: {}, timestamp: {}", request.plate(), request.direction(), request.timestamp());
        AccessEvent domain = mapper.fromRequestToDomain(request);
        log.debug("Mapped request to domain: {}", domain);
        AccessEvent accessEvent = validateAccessUseCase.execute(domain);
        log.debug("Access event after validation: {}", accessEvent);
        log.info("Access validation completed for plate: {}, result: {}", accessEvent.plate(), accessEvent.result());
        return mapper.fromDomainToResponse(accessEvent);
    }

    @CanReadAccessEvent
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AccessEventResponse> findAll(
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            Pageable pageable
    ) {
        log.info("Retrieving access history with filters - plate: {}, from: {}, to: {}, page: {}, size: {}", plate, from, to, pageable.getPageNumber(), pageable.getPageSize());
        PageInput pageInput = new PageInput(pageable.getPageNumber(), pageable.getPageSize(), plate, from, to);
        log.debug("PageInput created: {}", pageInput);
        PageOutput<AccessEvent> response = getAccessHistoryUseCase.execute(pageInput);
        log.debug("PageOutput received with {} elements", response.content().size());
        List<AccessEventResponse> content = response.content().stream().map(mapper::fromDomainToResponse).toList();
        log.debug("Mapped to {} responses", content.size());
        log.info("Retrieved {} access events", response.totalElements());
        return new PageImpl<>(content, pageable, response.totalElements());
    }
}
