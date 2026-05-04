package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.usecase.RegisterOwnerUseCase;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanWriteOwner;
import com.arthurscarpin.acs.infrastructure.mapper.OwnerMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.OwnerControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.OwnerRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.OwnerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController implements OwnerControllerDoc {

    private final RegisterOwnerUseCase registerOwnerUseCase;

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
}
