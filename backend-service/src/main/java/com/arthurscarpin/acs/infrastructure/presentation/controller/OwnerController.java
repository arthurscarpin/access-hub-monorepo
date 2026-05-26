package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.usecase.GetOwnersUseCase;
import com.arthurscarpin.acs.core.owner.usecase.RegisterOwnerUseCase;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanReadOwner;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanWriteOwner;
import com.arthurscarpin.acs.infrastructure.mapper.OwnerMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.OwnerControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.OwnerRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.OwnerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController implements OwnerControllerDoc {

    private final RegisterOwnerUseCase registerOwnerUseCase;

    private final GetOwnersUseCase getOwnersUseCase;

    private final OwnerMapper mapper;

    @CanWriteOwner
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerResponse save(@Valid @RequestBody OwnerRequest request) {
        log.info("Starting owner registration for email: {}", request.email());
        Owner domain = mapper.fromRequestToDomain(request);
        log.debug("Mapped request to domain: {}", domain);
        Owner response = registerOwnerUseCase.execute(domain);
        log.debug("Owner after registration: {}", response);
        log.info("Owner registration completed for email: {}", response.email());
        return mapper.fromDomainToResponse(response);
    }

    @CanReadOwner
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<OwnerResponse> findAll(Pageable pageable) {
        log.info("Starting owner listing - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        PageInput pageInput = new PageInput(pageable.getPageNumber(), pageable.getPageSize(), null, null, null);
        log.debug("Page input created: {}", pageInput);
        PageOutput<Owner> response = getOwnersUseCase.execute(pageInput);
        log.debug("Owners retrieved successfully - total elements: {}, total pages: {}", response.totalElements(), response.totalPages());
        List<OwnerResponse> content = response.content()
                .stream()
                .map(mapper::fromDomainToResponse)
                .toList();
        log.debug("Mapped {} owners to response", content.size());
        log.info("Owner listing completed successfully - returned {} records", content.size());
        return new PageImpl<>(content, pageable, response.totalElements());
    }
}
