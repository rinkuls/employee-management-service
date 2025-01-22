package com.employee.management.exception;

public class EmployeeNotFoundException extends RuntimeException {

  /**
   * @param message
   */
  public EmployeeNotFoundException(String message) {
    super(message);
  }
}
