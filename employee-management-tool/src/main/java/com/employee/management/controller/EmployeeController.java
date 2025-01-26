package com.employee.management.controller;

import com.employee.management.dto.EmployeeDetailsDTO;
import com.employee.management.exception.InvalidEmployeeDataException;
import com.employee.management.model.Employee;
import com.employee.management.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
  private final EmployeeService employeeService;

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


  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Details of all Employee sent successfully"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @Operation(
      summary = "Get List of all available employee",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @GetMapping("/getAll")
  @Transactional
  public ResponseEntity<List<EmployeeDetailsDTO>> getAllEmployees(
  ) {

    return Optional.ofNullable(employeeService.getAllEmployees())
        .filter(empList -> !empList.isEmpty()).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());


  }

}
