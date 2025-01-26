package com.employee.management.dto;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtValidationResult {

  private final boolean valid;
  private final String message;
  private final Claims claims;

  public static JwtValidationResult success(Claims claims) {
    return new JwtValidationResult(true, "Token is valid", claims);
  }

  public static JwtValidationResult failure(String message) {
    return new JwtValidationResult(false, message, null);
  }
}
