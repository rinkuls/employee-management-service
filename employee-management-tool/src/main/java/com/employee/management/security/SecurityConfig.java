package com.employee.management.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class SecurityConfig {

  public static final String AUTHORITY_ADMIN = "ROLE_ADMIN";
  public static final String AUTHORITY_USER = "ROLE_USER";
  @NonNull
  private final ToolUserAuthenticationEntryPointHandler toolUserAuthenticationEntryPointHandler;
  @NonNull
  private final ToolUserAccessDeniedHandler toolUserAccessDeniedHandler;
  @NonNull
  private final SecurityConfigProperties securityConfigProperties;


  @Bean
  public SecurityFilterChain resourceServerFilterChain(final HttpSecurity http,
      HandlerMappingIntrospector introspect) throws Exception {

    // ant matcher urls
    var antPathRequestMatcher = securityConfigProperties.allowedPaths().stream()
        .map(AntPathRequestMatcher::new)
        .toArray(AntPathRequestMatcher[]::new);

    // mvc url

    var mvcRequestMatcher = new MvcRequestMatcher(introspect, "/**");
    mvcRequestMatcher.setMethod(HttpMethod.OPTIONS);

    // restricted URL

    var mvcRequestMatcherForAddEmployee = new MvcRequestMatcher(introspect,
        "/api/v1/employee/addEmployee");
    mvcRequestMatcherForAddEmployee.setMethod(HttpMethod.POST);

    http.cors(Customizer.withDefaults()) // this is added for connection from UI
        .headers(headers -> headers.frameOptions(
            FrameOptionsConfig::sameOrigin)) // this line is added to h2 UI
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(antPathRequestMatcher)
            .permitAll()
            .requestMatchers(mvcRequestMatcher).permitAll()
            .requestMatchers(mvcRequestMatcherForAddEmployee).hasAuthority(AUTHORITY_ADMIN)
            .anyRequest().hasAnyAuthority(AUTHORITY_USER, AUTHORITY_ADMIN))
        .exceptionHandling(
            httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                .accessDeniedHandler(toolUserAccessDeniedHandler)
                .authenticationEntryPoint(toolUserAuthenticationEntryPointHandler))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
            .authenticationEntryPoint(toolUserAuthenticationEntryPointHandler))
        // sonar can cause issue
        .csrf(AbstractHttpConfigurer::disable)
        // sonar can cause issue
        .sessionManagement(
            sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
        .logout(AbstractHttpConfigurer::disable) // default spring logout feature
        .formLogin(AbstractHttpConfigurer::disable);// default spring login feature
    return http.build();
  }


  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {

    final JwtAndGrantedRolesConverter rolesConverter = new JwtAndGrantedRolesConverter();
    final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(rolesConverter);
    return jwtAuthenticationConverter;
  }


}