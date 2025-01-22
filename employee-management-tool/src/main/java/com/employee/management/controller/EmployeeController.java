package com.employee.management.controller;

import com.employee.management.dto.UserDetails;
import com.employee.management.exception.InvalidEmployeeDataException;
import com.employee.management.model.Employee;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/save")
@RequiredArgsConstructor
public class EmployeeController {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
  private final EmployeeService employeeService;
  private final UserService userService;


  @PostMapping
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee saved or updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid Employee data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @Operation(
      summary = "save employee record ",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<String> saveOrUpdateEmployee(@Valid @RequestBody Employee employee) {

    Optional.ofNullable(employee)
        .orElseThrow(() -> {
          logger.error("Employee data is missing.");
          return new InvalidEmployeeDataException("Employee data is missing.");
        });

    Optional.of(employee)
        .filter(emp -> StringUtils.hasText(emp.getName()) && emp.getEmpId() != null)
        .orElseThrow(() -> {
          logger.error("Employee name and Employee ID are required.");
          return new InvalidEmployeeDataException("Employee name and Employee ID are required.");
        });

    // Save or update the employee data
    employeeService.saveOrUpdateEmployee(employee);
    logger.info("Employee saved or updated successfully with ID: {}", employee.getEmpId());

    return ResponseEntity.status(HttpStatus.CREATED).body("Employee saved successfully.");
  }

  @PostMapping("/fetchRole")
  public ResponseEntity<UserDetails> fetchUserRole(
      @RequestHeader("Authorization") String authHeader) {
    try {
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        return ResponseEntity.ok(employeeService.FetchDetailsOfUser(token));
      }

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/addUserDetails")
  public ResponseEntity<String> addUserDetails(
      @Valid @RequestBody UserDetails userDetails) {

    Optional.ofNullable(userDetails.getName())
        .filter(StringUtils::hasText)
        .orElseThrow(() -> new InvalidEmployeeDataException("User name data is missing."));
    var retVal = userService.saveUserDetails(userDetails);

    return ResponseEntity.ok("User added successfully");


  }
}
