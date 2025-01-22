package com.employee.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * @param ex
   * @return
   */
  @ExceptionHandler(EmployeeNotFoundException.class)
  public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            "Please I am Sorry THE DATA IS NOT PRESENT IN MY DB YOU HAVE TO RAISE EXCEPTION IN KAFKA PRODUCER: "
                + ex.getMessage());
  }

  /**
   * @param ex
   * @return
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            "An unexpected error occurred During operation Please have patience and contact support : "
                + ex.getMessage());
  }

  /**
   * @param ex
   * @return
   */
  @ExceptionHandler(InvalidEmployeeDataException.class)
  public ResponseEntity<String> handleInvalidEmployeeDataException(
      InvalidEmployeeDataException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
