package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.usecase.LoginUserUseCase;
import com.arthurscarpin.acs.infrastructure.mapper.LoginMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.LoginControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.LoginRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController implements LoginControllerDoc {

    private final LoginUserUseCase loginUserUseCase;

    private final LoginMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("Starting login for email: {}", request.email());
        User domain = mapper.fromRequestToDomain(request);
        log.debug("Mapped request to domain: {}", domain);
        Object[] response = loginUserUseCase.execute(domain);
        log.debug("Login use case response: token length={}, expiresIn={}", ((String) response[0]).length(), response[1]);
        log.info("Login completed successfully for email: {}", request.email());
        return new LoginResponse((String) response[0], (Long) response[1]);
    }
}
