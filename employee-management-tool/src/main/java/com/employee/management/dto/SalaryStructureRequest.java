package com.employee.management.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SalaryStructureRequest {

  private Long employeeId;
  private BigDecimal basicSalary;
  private BigDecimal hra;
  private BigDecimal allowances;
  private BigDecimal deductions;
}
