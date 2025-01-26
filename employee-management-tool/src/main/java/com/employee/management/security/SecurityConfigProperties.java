package com.employee.management.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
public class SecurityConfigProperties {

  private String jwtSecret;
  private long tokenExpiration;

  // Getters and Setters
  public String getJwtSecret() {
    return jwtSecret;
  }

  public void setJwtSecret(String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  public long getTokenExpiration() {
    return tokenExpiration;
  }

  public void setTokenExpiration(long tokenExpiration) {
    this.tokenExpiration = tokenExpiration;
  }
}
