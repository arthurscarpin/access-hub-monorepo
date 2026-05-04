package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.usecase.RegisterUserUseCase;
import com.arthurscarpin.acs.infrastructure.mapper.UserMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.UserControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.UserRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final RegisterUserUseCase registerUserUseCase;

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
}
