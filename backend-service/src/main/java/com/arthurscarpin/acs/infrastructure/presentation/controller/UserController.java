package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.usecase.GetUsersUseCase;
import com.arthurscarpin.acs.core.user.usecase.RegisterUserUseCase;
import com.arthurscarpin.acs.infrastructure.mapper.UserMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.UserControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.UserRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.UserResponse;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final RegisterUserUseCase registerUserUseCase;

    private final GetUsersUseCase getUsersUseCase;

    private final UserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse save(@Valid @RequestBody UserRequest request) {
        log.info("Starting user registration for email: {}", request.email());
        User domain = mapper.fromRequestToDomain(request);
        log.debug("Mapped request to domain: {}", domain);
        User response = registerUserUseCase.execute(domain);
        log.debug("User after registration: {}", response);
        log.info("User registration completed for email: {}", response.email());
        return mapper.fromDomainToResponse(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> findAll(Pageable pageable) {
        log.info("Starting user search for pages: {}", pageable);
        PageInput pageInput = new PageInput(pageable.getPageNumber(), pageable.getPageSize(), null, null, null);
        log.debug("Page input created: {}", pageInput);
        PageOutput<User> response = getUsersUseCase.execute(pageInput);
        log.debug("User search completed for pages: {}", response);
        List<UserResponse> content = response.content()
                .stream()
                .map(mapper::fromDomainToResponse)
                .toList();
        log.debug("Mapped {} users to response", content.size());
        log.info("User listing completed successfully - returned {} records", content.size());
        return new PageImpl<>(content, pageable, response.totalElements());
    }
}
