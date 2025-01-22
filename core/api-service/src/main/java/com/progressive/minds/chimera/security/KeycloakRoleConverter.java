package com.progressive.minds.chimera.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakRoleConverter {

    private static final String CLIENT_ID = "chimera_api"; // Replace with your client ID

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convertRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null || !resourceAccess.containsKey(CLIENT_ID)) {
            return Collections.emptyList();
        }

        Object clientAccessObj = resourceAccess.get(CLIENT_ID);
        if (!(clientAccessObj instanceof Map)) {
            return Collections.emptyList();
        }
        Map<String, Object> clientAccess = (Map<String, Object>) clientAccessObj;

        Collection<String> roles = (Collection<String>) clientAccess.get("roles");

        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }
}



