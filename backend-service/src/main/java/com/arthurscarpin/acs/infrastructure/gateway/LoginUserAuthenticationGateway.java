package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.gateway.LoginGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginUserAuthenticationGateway implements LoginGateway {

    private final PasswordEncoder passwordEncoder;

    private final JwtEncoder jwtEncoder;

    @Override
    public boolean isPasswordCorrect(String password, String savedPassword) {
        return passwordEncoder.matches(password, savedPassword);
    }

    @Override
    public String generateToken(User savedUser, Long expiresIn, List<Scope> scopes) {
        List<String> scopesNames = scopes.stream()
                .map(Scope::name)
                .toList();

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("access-control-system")
                .subject(savedUser.name())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .issuedAt(Instant.now())
                .claim("email", savedUser.email())
                .claim("scope", String.join(" ", scopesNames))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();
    }

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
