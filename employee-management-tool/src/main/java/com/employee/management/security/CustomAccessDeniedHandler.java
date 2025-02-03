package com.employee.management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(final HttpServletRequest request, final HttpServletResponse response,
      final AccessDeniedException accessDeniedException) {

    log.warn("An unauthorized user (sub: {}) tried to access a protected URL: {}",
        SecurityContextHolder.getContext().getAuthentication().getName(), request.getRequestURI());
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }
}