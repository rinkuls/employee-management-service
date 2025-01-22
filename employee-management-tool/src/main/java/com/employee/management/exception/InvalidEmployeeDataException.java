package com.employee.management.exception;

public class InvalidEmployeeDataException extends RuntimeException {

  /**
   * @param message
   */
  public InvalidEmployeeDataException(String message) {
    super(message);
  }
}
