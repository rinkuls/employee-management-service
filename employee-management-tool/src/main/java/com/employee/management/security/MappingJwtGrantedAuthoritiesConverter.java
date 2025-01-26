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

  public static final String ROLES_TAG = "roles";
  public static final String CLAIM_NAME = "resource_access";
  public static final String RESOURCE_NAME = "atest";
  private static final String AUTHORITY_PREFIX = "SCOPE_";

  @Override
  public Collection<GrantedAuthority> convert(final @NonNull Jwt jwt) {
    return parseScopesClaim(jwt).stream().map(s -> AUTHORITY_PREFIX + s)
        .map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(HashSet::new));
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

