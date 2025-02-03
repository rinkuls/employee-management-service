package com.employee.management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(final HttpServletRequest request, final HttpServletResponse response,
      final AuthenticationException authException) {
    log.warn(
        "An unauthorized user (sub: {}) tried to access a protected URL in CustomAuthenticationEntryPointHandler: {}",
        SecurityContextHolder.getContext().getAuthentication().getName(), request.getRequestURI());

    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }
}
