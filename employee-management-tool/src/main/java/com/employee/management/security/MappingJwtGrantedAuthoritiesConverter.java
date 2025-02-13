package com.employee.management.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class MappingJwtGrantedAuthoritiesConverter implements
    Converter<Jwt, Collection<GrantedAuthority>> {


  public static final String CLAIM_NAME = "resource_access";
  public static final String RESOURCE_NAME = "employeeManagementTool";
  public static final String ROLES_TAG = "roles";
  private static final String AUTHORITY_PREFIX = "ROLE_";
  private static final String REALM_ACCESS = "realm_access";


  @Override
  public Collection<GrantedAuthority> convert(final @NonNull Jwt jwt) {
    Collection<String> roles = extractRolesFromRealmAccess(jwt);

    return roles.stream()
        .map(role -> AUTHORITY_PREFIX + role.toUpperCase()) // Convert to uppercase for consistency
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toCollection(HashSet::new));
  }


  @SuppressWarnings("unchecked")
  private Collection<String> extractRolesFromRealmAccess(final Jwt jwt) {
    Collection<String> mappedAuthorities = new ArrayList<>();

    if (jwt.hasClaim(REALM_ACCESS)) {
      Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS);
      if (realmAccess.containsKey(ROLES_TAG)) {
        mappedAuthorities.addAll((Collection<String>) realmAccess.get(ROLES_TAG));
      }
    }

    return mappedAuthorities;
  }

  @SuppressWarnings("unchecked")
  private Collection<String> parseScopesClaim(final Jwt jwt) {
    final Collection<String> mappedAuthorities = new ArrayList<>();
    if (jwt.hasClaim(CLAIM_NAME) && jwt.getClaim(CLAIM_NAME) != null) {
      final var resourceAccess = jwt.getClaimAsMap(CLAIM_NAME);
      final var resource = (Map<String, Object>) resourceAccess.get(RESOURCE_NAME);
      if (resource != null && resource.containsKey(ROLES_TAG)) {
        final var roles = (Collection<String>) resource.get(ROLES_TAG);
        if (roles != null) {
          mappedAuthorities.addAll(roles);
        }
      }
    }
    return mappedAuthorities;
  }


}

