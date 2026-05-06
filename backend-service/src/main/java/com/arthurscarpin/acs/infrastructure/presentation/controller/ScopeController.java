package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.scope.usecase.GetScopesUseCase;
import com.arthurscarpin.acs.infrastructure.mapper.ScopeMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.ScopeControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.response.ScopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scopes")
@RequiredArgsConstructor
public class ScopeController implements ScopeControllerDoc {

    private final GetScopesUseCase getScopesUseCase;

    private final ScopeMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScopeResponse> findAll() {
        return getScopesUseCase.execute().stream()
                .map(mapper::fromDomainToResponse)
                .toList();
    }
}
