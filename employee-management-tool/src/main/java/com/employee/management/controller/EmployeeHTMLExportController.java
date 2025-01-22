package com.employee.management.controller;

import com.employee.management.exception.InvalidEmployeeDataException;
import com.employee.management.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class EmployeeHTMLExportController {


  private static final Logger logger = LoggerFactory.getLogger(EmployeeHTMLExportController.class);
  private final EmployeeService employeeService;


  @Operation(
      summary = "Get an employee record as an HTML page with a given name",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Sample HTML page retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Sample page not found")
  })
  @GetMapping("/api/v1/html/sample/{name}")
  @Transactional
  public String GetSample(Model model, @PathVariable String name) {

    Optional.ofNullable(name).filter(StringUtils::hasText)
        .orElseThrow(() -> {

          logger.error(" Sample name is required.");
          return new InvalidEmployeeDataException("Sample ame is required.");
        });
    model.addAttribute("name", name);
    return "sample"; // Maps to sample.ftl
  }

  @Operation(
      summary = "Get an employee record as an HTML page by name",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee record retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Employee not found")
  })
  @GetMapping("/api/v1/html/employee/{name}")
  @Transactional
  public String GetEmployeeRecord(Model model, @PathVariable String name) {

    Optional.ofNullable(name).filter(StringUtils::hasText)
        .orElseThrow(() -> {

          logger.error("Employee name is required.");
          return new InvalidEmployeeDataException("Employee name is required.");
        });
    return employeeService.findByName(name).map(employee -> {
      model.addAttribute("employee", employee);
      return "employee";
    }).orElseGet(() -> {
      model.addAttribute("errorMessage", "Employee not found in our data base ");
      return "error";
    });

  }
}
