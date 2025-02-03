package com.employee.management.security;

import java.util.List;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security-config")
public record SecurityConfigProperties(
    List<String> allowedPaths,
    @NonNull CorsProperties cors) {

  public record CorsProperties(@NonNull List<String> allowedOrigins) {

  }


}
