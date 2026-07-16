package com.educativa.examenes.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Convierte el claim personalizado de rol (definido en Azure AD B2C como
 * atributo de extension, ej: "extension_Role") en un GrantedAuthority tipo
 * "ROLE_INSTRUCTOR" o "ROLE_ESTUDIANTE" para poder usar @PreAuthorize.
 *
 * Si el claim no viene en el token (usuario sin rol asignado en B2C), el
 * usuario queda autenticado pero sin authorities de rol, por lo que cualquier
 * endpoint con hasRole(...) le devolvera 403.
 */
@Component
public class RoleClaimAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final String roleClaim;
    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    public RoleClaimAuthenticationConverter(@Value("${app.security.role-claim}") String roleClaim) {
        this.roleClaim = roleClaim;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(defaultConverter.convert(jwt));

        String role = jwt.getClaimAsString(roleClaim);
        if (role != null && !role.isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase()));
        }

        return new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(jwt, authorities);
    }
}
