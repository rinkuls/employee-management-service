package com.employee.management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

  private static final int STATUS_UNAUTHORIZED = HttpServletResponse.SC_UNAUTHORIZED;
  private static final String CONTENT_TYPE_JSON = "application/json";
  private static final String UNKNOWN_USER = "UNKNOWN";

  private static final String UNAUTHORIZED_MESSAGE = """
      {
        "error": "Unauthorized: Your session has expired or you need to log in."
      }
      """;

  private static final String LOG_MESSAGE_UNAUTHORIZED =
      "🔐 Unauthorized Access: User '{}' attempted to access '{}'";

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    var username = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(auth -> auth.getName())
        .orElse(UNKNOWN_USER);

    log.warn(LOG_MESSAGE_UNAUTHORIZED, username,
        request.getRequestURI());

    // Return JSON response with 401 Unauthorized
    response.setStatus(STATUS_UNAUTHORIZED);
    response.setContentType(CONTENT_TYPE_JSON);
    response.getWriter().write(UNAUTHORIZED_MESSAGE);
  }
}
