package com.employee.management.exceptionHandler;

import com.employee.management.exception.EmployeeNotFoundException;
import com.employee.management.exception.InvalidEmployeeDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
    logger.error("An unexpected error occurred", ex);

    // Print the stack trace to standard error (useful for debugging)
    ex.printStackTrace();

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
