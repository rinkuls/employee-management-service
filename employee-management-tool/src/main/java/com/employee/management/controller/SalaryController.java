package com.employee.management.controller;

import com.employee.management.dto.SalaryStructureRequest;
import com.employee.management.model.SalaryRelease;
import com.employee.management.model.SalaryStructure;
import com.employee.management.service.SalaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/salary")
@RequiredArgsConstructor
public class SalaryController {

  private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

  private final SalaryService salaryService;

  // Retrieve salary structure by employee ID
  @GetMapping("/structure/{employeeId}")
  @Operation(
      summary = "Get salary structure by employee ID",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Salary structure retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Salary structure not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<SalaryStructure> getSalaryStructureByEmployeeId(
      @PathVariable Long employeeId) {
    logger.info("Fetching salary structure for employee ID: {}", employeeId);
    SalaryStructure salaryStructure = salaryService.getSalaryStructureByEmployeeId(employeeId);
    return ResponseEntity.ok(salaryStructure);
  }

  // Retrieve all salary releases for an employee
  @GetMapping("/releases/{employeeId}")
  @Operation(
      summary = "Get all salary releases for an employee",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Salary releases retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Salary releases not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<List<SalaryRelease>> getSalaryReleasesByEmployeeId(
      @PathVariable Long employeeId) {
    logger.info("Fetching salary releases for employee ID: {}", employeeId);
    List<SalaryRelease> salaryReleases = salaryService.getSalaryReleasesByEmployeeId(employeeId);
    return ResponseEntity.ok(salaryReleases);
  }

  // Release salary for a specific employee and month
  @PostMapping("/release/{employeeId}/{month}")
  @Operation(
      summary = "Release salary for a specific employee and month",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Salary released successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<SalaryRelease> releaseSalary(
      @PathVariable Long employeeId,
      @PathVariable String month) {
    logger.info("Releasing salary for employee ID: {} for month: {}", employeeId, month);
    YearMonth yearMonth = YearMonth.parse(month);
    SalaryRelease salaryRelease = salaryService.releaseSalary(employeeId, yearMonth);
    return ResponseEntity.ok(salaryRelease);
  }

  // Define or update salary structure
  @PostMapping("/structure")
  @Operation(
      summary = "Define or update salary structure",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Salary structure updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<SalaryStructure> defineOrUpdateSalaryStructure(
      @Valid @RequestBody SalaryStructureRequest request) {
    logger.info("Defining or updating salary structure: {}", request);
    SalaryStructure salaryStructure = salaryService.defineOrUpdateSalaryStructure(request);
    return ResponseEntity.ok(salaryStructure);
  }

  // Delete salary structure by employee ID
  @DeleteMapping("/structure/{employeeId}")
  @Operation(
      summary = "Delete salary structure by employee ID",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Salary structure deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Salary structure not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<String> deleteSalaryStructure(@PathVariable Long employeeId) {
    logger.info("Deleting salary structure for employee ID: {}", employeeId);
    salaryService.deleteSalaryStructureByEmployeeId(employeeId);
    return ResponseEntity.ok("Salary structure deleted successfully");
  }
}
