package com.employee.management.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class CorsConfig implements WebMvcConfigurer {

  @NonNull
  private final SecurityConfigProperties securityConfigProperties;

  @Override
  public void addCorsMappings(@NonNull final CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedMethods(HttpMethod.GET.name(),
            HttpMethod.PUT.name(),
            HttpMethod.POST.name(),
            HttpMethod.OPTIONS.name())
        .allowedOrigins(securityConfigProperties.cors().allowedOrigins().toArray(String[]::new));


  }


}
