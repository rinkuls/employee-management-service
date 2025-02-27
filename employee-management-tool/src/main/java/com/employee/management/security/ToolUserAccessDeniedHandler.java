package com.employee.management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ToolUserAccessDeniedHandler implements AccessDeniedHandler {

  private static final int STATUS_FORBIDDEN = HttpServletResponse.SC_FORBIDDEN;
  private static final String CONTENT_TYPE_JSON = "application/json";


  private static final String FORBIDDEN_MESSAGE = """
      {
        "error": "Access Denied: You do not have permission to access this resource."
      }
      """;
  private static final String LOG_MESSAGE_ACCESS_DENIED_AUTHENTICATED =
      "Access Denied: User '{}' (Roles: {}) tried to access '{}'";
  private static final String LOG_MESSAGE_ACCESS_DENIED_UNAUTHENTICATED =
      "Access Denied: An unauthenticated request tried to access '{}'";

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException ex) throws IOException {

    var authentication = Optional.ofNullable(
        SecurityContextHolder.getContext().getAuthentication());

    authentication.ifPresentOrElse(
        auth -> log.warn(LOG_MESSAGE_ACCESS_DENIED_AUTHENTICATED,
            auth.getName(), auth.getAuthorities(), request.getRequestURI()),
        () -> log.warn(LOG_MESSAGE_ACCESS_DENIED_UNAUTHENTICATED,
            request.getRequestURI())
    );

    response.setStatus(STATUS_FORBIDDEN);
    response.setContentType(CONTENT_TYPE_JSON);
    response.getWriter().write(FORBIDDEN_MESSAGE);
  }
}
