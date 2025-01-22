package com.employee.management.security;

import com.employee.management.dto.JwtValidationResult;
import com.employee.management.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

  private final JwtValidator jwtValidator;
  private final UserDetailsService userDetailsService;
  private final List<AntPathRequestMatcher> skipPaths = List.of(
      new AntPathRequestMatcher("/h2/**"),
      new AntPathRequestMatcher("/swagger-ui/**"),
      new AntPathRequestMatcher("/v3/api-docs/**")
  );

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return skipPaths.stream().anyMatch(matcher -> matcher.matches(request));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    var token = extractToken(request);
    var username = jwtValidator.getUsernameFromToken(token);

    // Once we get the token validate it.
    UserDetails userDetails = null;
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      userDetails = userDetailsService.loadUserByUsername(username);

    }
    if (token != null && jwtValidator.validateTokenByUserDetails(token, userDetails)) {

      JwtValidationResult result = jwtValidator.getJwtValidationResult(token);

      if (result.isValid()) {
        Claims claims = result.getClaims();
        // You can directly use claims like username, roles, etc.
        request.setAttribute("username", claims.get("username"));
        request.setAttribute("role", claims.get("role"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(result.getMessage());
        return;
      }
    }
try {
  filterChain.doFilter(request, response);
} catch (Exception e) {
  throw new RuntimeException(e);
}

  }

  private String extractToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");

    String jwtToken = null;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwtToken = authHeader.substring(7);

    } else {
      logger.warn("JWT Token does not begin with Bearer String");
      throw new InvalidJwtTokenException("JWT Token does not begin with Bearer String");
    }

    return jwtToken;
  }


}
