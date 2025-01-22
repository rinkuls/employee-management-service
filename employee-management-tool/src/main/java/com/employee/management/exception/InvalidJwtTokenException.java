package com.employee.management.exception;

public class InvalidJwtTokenException extends RuntimeException {

  /**
   * @param message
   */
  public InvalidJwtTokenException(String message) {
    super(message);
  }
}
