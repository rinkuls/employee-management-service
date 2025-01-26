package com.employee.management.controller;

import com.employee.management.exception.InvalidEmployeeDataException;
import com.employee.management.model.EmployeeLeave;
import com.employee.management.service.EmployeeLeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee/leave")
@RequiredArgsConstructor
public class EmployeeLeaveManagementController {

  private static final Logger logger = LoggerFactory.getLogger(
      EmployeeLeaveManagementController.class);
  private final EmployeeLeaveService employeeLeaveService;


  @GetMapping("/getDetails/EmpIDAndFinancialYear/{empId}/{financialYear}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee Leave details found successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid Employee data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @Operation(
      summary = "Employee Leave details found successfully",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<EmployeeLeave> getLeavesDetailsByEmpIDAndFinancialYear(
      @PathVariable Long empId, @PathVariable Integer financialYear) {

    Optional.ofNullable(empId).filter(Id -> Id < 0 && Id < 10000)
        .orElseThrow(() -> new InvalidEmployeeDataException(
            "Employee name is not in proper format or its null."));
    logger.info("Employee Leave for : {}", empId);

    Optional.ofNullable(financialYear)
        .orElseThrow(() -> new InvalidEmployeeDataException("Year is null"));

    return employeeLeaveService.findLeaveDetailsByEmpIDAndFinancialYear(empId, financialYear)
        .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/getDetails/{empId}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee Leave details found successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid Employee data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @Operation(
      summary = "Employee Leave details found successfully",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<List<EmployeeLeave>> getEmployeeLeavesDetailsByEmpId(
      @PathVariable Long empId) {

    Optional.ofNullable(empId).filter(id -> id > 0 && id < 10000)
        .orElseThrow(() -> new InvalidEmployeeDataException(
            "Employee empId is not in proper format or its null."));
    logger.info("Employee Leave for : {}", empId);

    var leaves = employeeLeaveService.findLeaveDetailsByEmpID(empId);

    return leaves.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/getDetails/{name}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee Leave details found successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid Employee data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @Operation(
      summary = "Employee Leave details found successfully",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<List<EmployeeLeave>> getEmployeeLeavesDetailsByName(
      @PathVariable String name) {

    Optional.ofNullable(name).filter(StringUtils::hasText)
        .orElseThrow(() -> new InvalidEmployeeDataException(
            "Employee name is not in proper format or its null."));
    logger.info("Employee Leave for : {}", name);

    var leaves = employeeLeaveService.findLeaveDetailsByName(name);

    return leaves.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }


  @PostMapping("/addLeaves/financialYear")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee Leave details added successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid Employee data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @Operation(
      summary = "Employee Leave details added successfully",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<String> addOrUpdateEmployeeLeavesForFinancialYear(
      @Valid @RequestBody EmployeeLeave employeeLeave) {

    Optional.ofNullable(employeeLeave)
        .orElseThrow(() -> new InvalidEmployeeDataException(
            "employee Leave  is not in proper format or its null."));
    logger.info("Employee Leave details are wrong : {}", employeeLeave.getFinancialYear());

    employeeLeaveService.addOrUpdateEmployeeLeavesForFinancialYear(employeeLeave);

    return ResponseEntity.ok("Added leaves successfully");
  }
}
