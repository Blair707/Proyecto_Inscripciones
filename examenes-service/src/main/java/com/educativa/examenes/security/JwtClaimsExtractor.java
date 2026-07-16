package com.educativa.examenes.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtClaimsExtractor {

    @Value("${app.security.role-claim}")
    private String roleClaim;

    public Jwt getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        return null;
    }

    public String getEmail() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString("emails") : null;
    }

    public String getSub() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getSubject() : null;
    }

    public String getNombre() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString("given_name") : null;
    }

    public String getRole() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString(roleClaim) : null;
    }
}
