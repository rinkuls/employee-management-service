package com.employee.management.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtValidationFilter jwtValidationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        // Disable CSRF for H2 console
        .csrf(AbstractHttpConfigurer::disable)
        // Configure headers to allow frames for H2 console
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
        // Configure authorization rules
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2/**"),
        AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
        AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
        AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
        AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
        AntPathRequestMatcher.antMatcher("/webjars/**"))
        .permitAll()
        .anyRequest().authenticated()
        )
        .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:4200"); // Allow Angular app origin
    configuration.addAllowedMethod("*"); // Allow all HTTP methods
    configuration.addAllowedHeader("*"); // Allow all headers
    configuration.setAllowCredentials(true); // Allow credentials like cookies

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); // Apply CORS rules to all endpoints
    return source;
  }
}
